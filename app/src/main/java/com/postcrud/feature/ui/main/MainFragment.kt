package com.postcrud.feature.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.postcrud.PAGE_SIZE

import com.postcrud.R
import com.postcrud.component.network.isNetworkConnect
import com.postcrud.core.api.PostsApi
import com.postcrud.core.api.ProfileApi
import com.postcrud.core.utils.toast
import com.postcrud.feature.data.adapters.PostRecyclerAdapter
import com.postcrud.feature.data.dto.PostResponseDto
import com.postcrud.feature.data.dto.user.UserResponseDto
import com.postcrud.feature.data.factory.*
import io.ktor.util.KtorExperimentalAPI
import kotlinx.android.synthetic.main.dialog_create_post.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.get

@KtorExperimentalAPI
class MainFragment : Fragment(R.layout.fragment_main) {
    private val posts: PostsApi = get()
    private val users: ProfileApi = get()
    private val postsList: MutableList<PostResponseDto> =
        emptyArray<PostResponseDto>().toMutableList()
    private var pageId: Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        loadLastPage()
        //fetchData()
        setList()
        getUserProfile()
        setSwipeRefresh()
        super.onViewCreated(view, savedInstanceState)

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

    private fun setList() =
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                with(recyclerListPosts) {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = PostRecyclerAdapter(postsList, {
                        pushLike(it.id)
                    }, {
                        pushDislike(it.id)
                    }, {
                        createRepost(it.id)
                    })

                    val scrollListener = object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
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
            onCreatePostSuccess(post)
        } catch (e: Exception) {
            networkError(e)
        }
    }

    private fun loadLastPage() {
        changeProgressState(true)
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val pageList = posts.getLastPage(PAGE_SIZE)
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

    private fun addPostsInList(list: List<PostResponseDto>) = viewLifecycleOwner.lifecycleScope.launch {
        withContext(Dispatchers.Main) {
            postsList.addAll(list)
            notifyDataChangeAdapter()
        }
    }

    private fun setFloatActionButton(user: UserResponseDto) =
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                createNewPostButton.setOnClickListener {
                    showDialog {
                        val post: PostResponseDto = creteNewPost(
                            contentText = it, author = user.username,
                            ownerId = user.id
                        )
                        createPost(post)
                    }
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

    private fun onCreatePostSuccess(postResponseDto: PostResponseDto) =
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                postsList.add(postResponseDto)
                notifyDataChangeAdapter()
                toast(getString(R.string.publishPost))
            }
        }

    private fun changeProgressState(state: Boolean) {
        when (state) {
            true -> indeterminateBar.show()
            false -> indeterminateBar.hide()
        }
    }

    private fun networkError(e: Exception) = viewLifecycleOwner.lifecycleScope.launch {
        withContext(Dispatchers.Main) {
            if (!isNetworkConnect(requireContext())) errorNoNetwork.visibility = View.VISIBLE
            else toast(e.localizedMessage!!)
        }

    }
}
