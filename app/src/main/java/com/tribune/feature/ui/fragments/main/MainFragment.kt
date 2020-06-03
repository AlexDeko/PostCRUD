package com.tribune.feature.ui.fragments.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.iid.FirebaseInstanceId
import com.tribune.*

import com.tribune.component.network.isNetworkConnect
import com.tribune.core.api.PostsApi
import com.tribune.core.api.ProfileApi
import com.tribune.feature.ui.adapters.PostRecyclerAdapter
import com.tribune.feature.data.dto.PostResponseDto
import com.tribune.feature.data.dto.user.UserResponseDto
import com.tribune.component.creator.*
import com.tribune.component.notification.NotificationHelper
import com.tribune.component.notification.UserNotHereWorker
import com.tribune.core.state.UiState
import com.tribune.core.utils.*
import com.tribune.feature.data.dto.PostResponseDto.Companion.toDto
import com.tribune.feature.data.dto.PostResponseDto.Companion.toModel
import com.tribune.feature.data.model.PostModel
import com.tribune.feature.data.model.PostType
import com.tribune.feature.ui.adapters.diff_util.PostDiffUtilResult
import io.ktor.util.KtorExperimentalAPI
import kotlinx.android.synthetic.main.dialog_create_post.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.get
import java.util.concurrent.TimeUnit

@KtorExperimentalAPI
class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel = MainViewModel()

    private val posts: PostsApi = get()
    private val users: ProfileApi = get()

    // private val media: MediaApi = get()
    private val postsList: MutableList<PostModel> =
        emptyArray<PostModel>().toMutableList()
    private var pageId: Long = 0

    //private val REQUEST_IMAGE_CAPTURE = 1
    //   private var imageBitmap: Bitmap? = null
    //private var imageId: String? = null
    //  private lateinit var sharedPreferences: SharedPreferences
    private var tokenFirebase = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requestToken()
        scheduleJob()
        loadLastPage()
        setList()
        getUserProfile()
        setSwipeRefresh()


        viewModel.uiModel.observe(viewLifecycleOwner) { state ->
            when (state) {
                UiState.Empty -> {
                    postsList.clear()
                    notifyDataChangeAdapter()
                }
                UiState.NotFound -> {
                    changeProgressState(true)
                    toast(getString(R.string.not_found))
                    changeProgressState(false)
                }
                is UiState.Data -> {

                }
                is UiState.Error -> {

                }
                UiState.EmptyProgress -> {

                }
                is UiState.Refreshing.Data -> {
                    changeProgressState(false)
                }
                UiState.Refreshing.Empty -> {
                    changeProgressState(true)
                    setEmptyListForRecycler()
                }
                is UiState.Refreshing.Error -> {
                    changeProgressState(false)
                    toast(getString(R.string.error))
                    setEmptyListForRecycler()
                }
            }
        }

    }

    private fun setEmptyListForRecycler() {
        postsList.clear()
        notifyDataChangeAdapter()
    }

    private fun requestToken() {
        with(GoogleApiAvailability.getInstance()) {
            val code = isGooglePlayServicesAvailable(requireContext())
            if (code == ConnectionResult.SUCCESS) {
                FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
                    // viewLifecycleOwner.lifecycleScope.launch {
                    tokenFirebase = it.token
                    //sharedPreferences.putString(PREFS_TOKEN_FIREBASE, it.token)
                    arguments?.putString(ARG_TOKEN_FIREBASE, tokenFirebase)
                    //}
                }
                return@with
            }

            if (isUserResolvableError(code)) {
                getErrorDialog(activity, code, 9000).show()
                return
            }

            snack(getString(R.string.google_play_unavailable))
            return
        }
    }


    private fun setSwipeRefresh() {
        swipeRefresh.setOnRefreshListener {
            postsList.clear()
            loadLastPage()
            notifyDataChangeAdapter()
            swipeRefresh.isRefreshing = false
        }
    }

    private fun notifyDataChangeAdapter() {
        PostDiffUtilResult().getDIffUtilResult(
            recyclerAdapter = recyclerListPosts.adapter as PostRecyclerAdapter,
            baseListNote = postsList.asReversed()
        )
    }

    private fun setList() {
        with(recyclerListPosts) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = PostRecyclerAdapter(
                postsList.asReversed(),
                onLikeClicked = {
                    pushLike(it.id)
                },
                onDislikeClicked = {
                    pushDislike(it.id)
                },
                onRepostClicked = {
                    createRepost(it.id)
                }
            )

            val scrollListener = object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager: LinearLayoutManager =
                        recyclerView.layoutManager as LinearLayoutManager
                    if (layoutManager.findLastVisibleItemPosition() >= layoutManager.itemCount - 1
                    ) {
                        pageId = postsList.lastIndex.toLong()
                        fetchData()
                    }
                }
            }
            addOnScrollListener(scrollListener)
        }
    }

    private fun pushLike(id: Long) = viewLifecycleOwner.lifecycleScope.launch {
        try {
            posts.setLikePost(id)
        } catch (e: Exception) {
            networkError(e)
        }
    }


    private fun pushDislike(id: Long) = viewLifecycleOwner.lifecycleScope.launch {
        try {
            posts.setDislikePost(id)
        } catch (e: Exception) {
            networkError(e)
        }
    }

    private fun createRepost(id: Long) = viewLifecycleOwner.lifecycleScope.launch {
        try {
            val post = posts.createRepost(id).toModel()
            val repost = posts.getPostsById(post.parentId!!)
            val targetRepost = post.copy(repost = repost, author = users.getProfile().username)
            onCreatePostSuccess(targetRepost)
        } catch (e: Exception) {
            networkError(e)
        }
    }

    private fun loadLastPage() {
        changeProgressState(true)
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val pageList = posts.getLastPage(PAGE_SIZE).map { it.toModel() }
                if (pageList.isNotEmpty()) {
                    for (i in pageList.indices) {
                        if (pageList[i].type == PostType.REPOST)
                            pageList[i].repost = posts.getPostsById(pageList[i].parentId!!)
                    }
                }

                addPostsInList(pageList)

            } catch (e: Exception) {
                networkError(e)
            }
        }
        changeProgressState(false)
    }


    @KtorExperimentalAPI
    private fun fetchData() {
        changeProgressState(true)
        viewLifecycleOwner.lifecycleScope.launch {

            try {
                val pageList = posts.getPage(pageId, PAGE_SIZE).map { it.toModel() }
                addPostsInList(pageList)

            } catch (e: Exception) {
                networkError(e)
            }

        }
        changeProgressState(false)
    }

    private fun addPostsInList(list: List<PostModel>) {
        postsList.addAll(list.asReversed())
        notifyDataChangeAdapter()
    }

    private fun setFloatActionButton(user: UserResponseDto) {
        createNewPostButton.setOnClickListener {
            // showDialog {
            DialogFragment().show(parentFragmentManager, this.tag)
            val postId = arguments?.getLong(ARG_POST_ID)
            if (postId != null) {
                addPostInList(id = postId)
            }

//            val post: PostResponseDto = creteNewPost(
//                contentText = it, author = user.username,
//                ownerId = user.id, imageId = arguments?.getString(ARG_IMAGE_ID)
//            )

            // createPost(post.toModel())

            //createPost(arguments[])
        }
        // }
    }

    private fun addPostInList(id: Long) = viewLifecycleOwner.lifecycleScope.launch {
        try {
            val post = posts.getPostsById(id)
            addPost(post.toModel())
        } catch (e: Exception) {
            networkError(e)
        }
    }

    private fun addPost(post: PostModel) {
        postsList.add(post)
        notifyDataChangeAdapter()
    }

//    private fun showDialog(createBtnClicked: (content: String) -> Unit) {
//        val dialog = AlertDialog.Builder(requireContext())
//            .setView(R.layout.dialog_create_post)
//            .setCancelable(false)
//            .show()
//
//        with(dialog) {
//            createPostButton.setOnClickListener {
//                createBtnClicked(textPostInput.text.toString())
//                hide()
//            }
//            cancelButton.setOnClickListener {
//                cancel()
//            }
//            addPhotoButton.setOnClickListener {
//                dispatchTakePictureIntent()
//                addPhotoButton.isEnabled = false
//            }
//        }
//    }

//    private fun dispatchTakePictureIntent() {
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
//                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//            }
//        }
//    }

    private fun getUserProfile() = viewLifecycleOwner.lifecycleScope.launch {
        try {
            setFloatActionButton(users.getProfile())
        } catch (e: Exception) {
            networkError(e)
        }
    }


    private fun createPost(postModel: PostModel) =
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val token = arguments?.getString(ARG_TOKEN_FIREBASE).orEmpty()
                val post = posts.createPost(
                    postModel.toDto(), token
                )
                onCreatePostSuccess(post.toModel())
            } catch (e: Exception) {
                networkError(e)
            }
        }

    private fun onCreatePostSuccess(postModel: PostModel) {
        postsList.add(postModel)
        notifyDataChangeAdapter()
        toast(getString(R.string.publishPost))
    }

    private fun changeProgressState(state: Boolean) {
        when (state) {
            true -> indeterminateBar.show()
            false -> indeterminateBar.hide()
        }
    }

    private fun networkError(e: Exception) {
        if (!isNetworkConnect(requireContext())) errorNoNetwork.visibility = View.VISIBLE
        else toast(e.localizedMessage!!)
    }

//    private fun pushMediaImage(bitmap: Bitmap) = viewLifecycleOwner.lifecycleScope.launch {
//        val bos = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
//        val reqFIle = RequestBody.create("image/jpeg".toMediaTypeOrNull(), bos.toByteArray())
//        val body = MultipartBody.Part.createFormData("file", "image.jpg", reqFIle)
//        try {
//            val mediaImage = media.setMediaPost(body)
//            imageId = mediaImage.id
//            NotificationHelper.mediaUploaded(mediaImage, requireContext())
//        } catch (e: Exception) {
//            networkError(e)
//        }
//
//    }

    private fun scheduleJob() {
        val checkWork =
            PeriodicWorkRequestBuilder<UserNotHereWorker>(
                SHOW_NOTIFICATION_AFTER_UNVISITED_MS,
                TimeUnit.MILLISECONDS
            ).build()

        WorkManager.getInstance(requireContext())
            .enqueueUniquePeriodicWork(
                "user_present_work",
                ExistingPeriodicWorkPolicy.KEEP, checkWork
            )
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFirstTimeWork(requireContext())) {
            NotificationHelper.comeBackNotification(requireContext())
            setLastVisitTimeWork(requireContext(), System.currentTimeMillis())
        } else {
            setLastVisitTimeWork(requireContext(), System.currentTimeMillis())
        }
    }

//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
//            imageBitmap = data?.extras?.get("data") as Bitmap
//            pushMediaImage(imageBitmap!!)
//        }
//    }
}
