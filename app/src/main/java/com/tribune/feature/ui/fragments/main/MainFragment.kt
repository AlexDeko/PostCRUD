package com.tribune.feature.ui.fragments.main

import android.os.Bundle
import android.view.View
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
import com.tribune.component.notification.NotificationHelper
import com.tribune.component.notification.UserNotHereWorker
import com.tribune.core.state.UiState
import com.tribune.core.utils.*
import com.tribune.feature.data.dto.PostResponseDto.Companion.toModel
import com.tribune.feature.data.model.PostModel
import com.tribune.feature.data.model.PostType
import com.tribune.feature.ui.adapters.diff_util.PostDiffUtilResult
import com.tribune.feature.ui.fragments.dialog.DialogCreatePostFragments
import io.ktor.util.KtorExperimentalAPI
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.get
import java.util.concurrent.TimeUnit

@KtorExperimentalAPI
class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel = MainViewModel()
    private val posts: PostsApi = get()
    private val users: ProfileApi = get()
    private val postsList: MutableList<PostModel> =
        emptyArray<PostModel>().toMutableList()
    private var pageId: Long = 0
    private var tokenFirebase = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requestToken()
        scheduleJob()
        loadLastPage()
        setList()
        setFloatActionButton()
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
                    tokenFirebase = it.token
                    arguments?.putString(ARG_TOKEN_FIREBASE, tokenFirebase)
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
                onApproveClicked = {
                    pushApprove(it.id)
                },
                onNotApproveClicked = {
                    pushNotApprove(it.id)
                },
                unselectedApprovesClicked = {
                    pushUnselectedApproves(it.id)
                },
                onRepostClicked = {
                    createRepost(it.id)
                },
                setBadge = { id, isApprovedThisPost, cancelApproved, cancelNotApproved ->

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

    private fun pushApprove(id: Long) = viewLifecycleOwner.lifecycleScope.launch {
        try {
            posts.setApprovePost(id)
        } catch (e: Exception) {
            networkError(e)
        }
    }


    private fun pushNotApprove(id: Long) = viewLifecycleOwner.lifecycleScope.launch {
        try {
            posts.setNotApprovePost(id)
        } catch (e: Exception) {
            networkError(e)
        }
    }

    private fun pushUnselectedApproves(id: Long) = viewLifecycleOwner.lifecycleScope.launch {
        try {
            posts.setUnselectedApproves(id)
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

    private fun setFloatActionButton() {
        createNewPostButton.setOnClickListener {
            DialogCreatePostFragments().show(parentFragmentManager, this.tag)
            val postId = arguments?.getLong(ARG_POST_ID)
            if (postId != null) {
                addPostInList(id = postId)
            }
        }

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
}
