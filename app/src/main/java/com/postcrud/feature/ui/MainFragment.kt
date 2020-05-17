package com.postcrud.feature.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager

import com.postcrud.R
import com.postcrud.core.api.PostsApi
import com.postcrud.core.utils.toast
import com.postcrud.feature.data.adapters.PostRecyclerAdapter
import com.postcrud.feature.data.dto.PostResponseDto
import io.ktor.util.KtorExperimentalAPI
import kotlinx.android.synthetic.main.dialog_create_post.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.get

@KtorExperimentalAPI
class MainFragment : Fragment(R.layout.fragment_main) {
    private val posts: PostsApi = get()

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
                toast(e.localizedMessage)
                toast(getString(R.string.request_internet_false))
            }
            changeProgressState(false)
        }

    private fun setFloatActionButton() {
        createNewPostButton.setOnClickListener {

        }
    }

    fun showDialog(createBtnClicked: (content: String) -> Unit) {
        val dialog = AlertDialog.Builder(requireContext())
            .setView(R.layout.dialog_create_post)
            .show()
        dialog.createPostButton.setOnClickListener {

            //createBtnClicked(dialog.contentEdt.text.toString())
            dialog.dismiss()
        }
    }

    private fun changeProgressState(state: Boolean) {
        when (state) {
            true -> indeterminateBar.show()
            false -> indeterminateBar.hide()
        }
    }
}
