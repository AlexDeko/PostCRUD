package com.postcrud.feature.ui.main

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.postcrud.PAGE_SIZE

import com.postcrud.R
import com.postcrud.component.network.isNetworkConnect
import com.postcrud.core.api.MediaApi
import com.postcrud.core.api.PostsApi
import com.postcrud.core.api.ProfileApi
import com.postcrud.core.utils.toast
import com.postcrud.feature.ui.adapters.PostRecyclerAdapter
import com.postcrud.feature.data.dto.PostResponseDto
import com.postcrud.feature.data.dto.user.UserResponseDto
import com.postcrud.component.factory.*
import com.postcrud.feature.data.model.PostType
import io.ktor.util.KtorExperimentalAPI
import kotlinx.android.synthetic.main.dialog_create_post.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.android.ext.android.get
import java.io.ByteArrayOutputStream

@KtorExperimentalAPI
class MainFragment : Fragment(R.layout.fragment_main) {
    private val posts: PostsApi = get()
    private val users: ProfileApi = get()
    private val media: MediaApi = get()
    private val postsList: MutableList<PostResponseDto> =
        emptyArray<PostResponseDto>().toMutableList()
    private var pageId: Long = 0
    private val REQUEST_IMAGE_CAPTURE = 1
    private var imageBitmap: Bitmap? = null
    private var imageId: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadLastPage()
        setList()
        getUserProfile()
        setSwipeRefresh()
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
        recyclerListPosts.adapter?.notifyDataSetChanged()
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
                },
                imageBitmap = imageBitmap
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
            val post = posts.createRepost(id)
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
                val pageList = posts.getLastPage(PAGE_SIZE)
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
                val pageList = posts.getPage(pageId, PAGE_SIZE)
                addPostsInList(pageList)

            } catch (e: Exception) {
                networkError(e)
            }

        }
        changeProgressState(false)
    }

    private fun addPostsInList(list: List<PostResponseDto>) {
        postsList.addAll(list)
        notifyDataChangeAdapter()
    }

    private fun setFloatActionButton(user: UserResponseDto) {
        createNewPostButton.setOnClickListener {
            showDialog {
                val post: PostResponseDto = creteNewPost(
                    contentText = it, author = user.username,
                    ownerId = user.id, imageId = imageId
                )
                createPost(post)
            }
        }
    }

    private fun showDialog(createBtnClicked: (content: String) -> Unit) {
        val dialog = AlertDialog.Builder(requireContext())
            .setView(R.layout.dialog_create_post)
            .setCancelable(false)
            .show()

        with(dialog) {
            createPostButton.setOnClickListener {
                createBtnClicked(textPostInput.text.toString())
                hide()
            }
            cancelButton.setOnClickListener {
                cancel()
            }
            addPhotoButton.setOnClickListener {
                dispatchTakePictureIntent()
                addPhotoButton.isEnabled = false
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun getUserProfile() = viewLifecycleOwner.lifecycleScope.launch {
        setFloatActionButton(users.getProfile())
    }


    private fun createPost(postResponseDto: PostResponseDto) =
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val post = posts.createOrUpdatePost(postResponseDto)
                onCreatePostSuccess(post)
            } catch (e: Exception) {
                networkError(e)
            }
        }

    private fun onCreatePostSuccess(postResponseDto: PostResponseDto) {
        postsList.add(postResponseDto)
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

    private fun pushMediaImage(bitmap: Bitmap) = viewLifecycleOwner.lifecycleScope.launch {
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val reqFIle = RequestBody.create("image/jpeg".toMediaTypeOrNull(), bos.toByteArray())
        val body = MultipartBody.Part.createFormData("file", "image.jpg", reqFIle)
        try {
            val mediaImage = media.setMediaPost(body)
            imageId = mediaImage.id
        } catch (e: Exception) {
            networkError(e)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as Bitmap
            pushMediaImage(imageBitmap!!)
        }
    }
}
