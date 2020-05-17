package com.postcrud.feature.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.postcrud.R
import com.postcrud.core.BaseFragment
import com.postcrud.core.api.PostsApi
import com.postcrud.core.utils.toast
import com.postcrud.feature.data.adapters.PostRecyclerAdapter
import com.postcrud.feature.data.dto.PostResponseDto
import io.ktor.util.KtorExperimentalAPI
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.get

class MainFragment : BaseFragment(), CoroutineScope by MainScope() {
    private val posts: PostsApi = get()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fetchData()

        super.onViewCreated(view, savedInstanceState)
    }

    @KtorExperimentalAPI
    private fun setList(list: MutableList<PostResponseDto>) = launch {
        withContext(Dispatchers.Main) {
            with(recyclerListPosts) {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = PostRecyclerAdapter(list)
            }

        }
    }

    @KtorExperimentalAPI
    private fun fetchData() = launch {
        changeProgressState(true)
        try {

            val list = posts.getPosts()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doFinally {
//                    changeProgressState(false)
//                }
//                .subscribe({ postList ->
//                    setList(list = postList.toMutableList())
//
//
//                }, {
//
//                    Toast.makeText(context, it.localizedMessage, Toast.LENGTH_LONG).show()
//                })
//                .addTo(compositeDisposable)
            setList(list = list.toMutableList())

        } catch (e: Exception) {
            toast(getString(R.string.request_internet_false))
        }
        changeProgressState(false)
    }

    private fun changeProgressState(state: Boolean) {
        when (state) {
            true -> indeterminateBar.show()
            false -> indeterminateBar.hide()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}
