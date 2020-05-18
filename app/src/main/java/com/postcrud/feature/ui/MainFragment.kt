package com.postcrud.feature.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager

import com.postcrud.R
import com.postcrud.core.api.PostsApi
import com.postcrud.core.utils.toast
import com.postcrud.feature.data.adapters.PostRecyclerAdapter
import com.postcrud.feature.data.dto.PostResponseDto
import com.postcrud.feature.data.factory.PostFactory
import io.ktor.util.KtorExperimentalAPI
import kotlinx.android.synthetic.main.dialog_create_post.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.get

@KtorExperimentalAPI
class MainFragment : Fragment(R.layout.fragment_main) {
    private val posts: PostsApi = get()
    private val prefs: SharedPreferences = get()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fetchData()
        setFloatActionButton()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setList(list: MutableList<PostResponseDto>) =
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                with(recyclerListPosts) {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = PostRecyclerAdapter(list)
                }

            }
        }

    @KtorExperimentalAPI
    private fun fetchData() =
        viewLifecycleOwner.lifecycleScope.launch {
            changeProgressState(true)
            try {

                val list = posts.getPosts()
                setList(list = list.toMutableList())

            } catch (e: Exception) {
                toast(e.localizedMessage!!)
            }
            changeProgressState(false)
        }

    private fun setFloatActionButton() {
        createNewPostButton.setOnClickListener {
            showDialog {
                val postResponseDto: PostResponseDto = PostFactory().creteNewPost(it)

            }
        }
    }

    fun showDialog(createBtnClicked: (content: String) -> Unit) {
        val dialog = AlertDialog.Builder(requireContext())
            .setView(R.layout.dialog_create_post)
            .show()
        with(dialog) {
            createPostButton.setOnClickListener {
                createBtnClicked(textPostInput.text.toString())
                dialog.hide()
            }
            cancelButton.setOnClickListener {
                cancel()
            }
        }
    }

    fun createPost(postResponseDto: PostResponseDto) = viewLifecycleOwner.lifecycleScope.launch {
        try {
            posts.createOrUpdatePost(postResponseDto)
        } catch (e: Exception) {

        }
    }

    fun onCreatePostError(e: Exception) {
        toast(e.localizedMessage!!)
    }

    private fun changeProgressState(state: Boolean) {
        when (state) {
            true -> indeterminateBar.show()
            false -> indeterminateBar.hide()
        }
    }
}
