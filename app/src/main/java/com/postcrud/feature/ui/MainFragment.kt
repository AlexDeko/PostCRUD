package com.postcrud.feature.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import com.postcrud.R
import com.postcrud.core.BaseFragment
import com.postcrud.core.api.NewsApi
import com.postcrud.feature.data.adapters.PostRecyclerAdapter
import com.postcrud.feature.data.dto.PostResponseDto
import io.ktor.util.KtorExperimentalAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.get

class MainFragment : BaseFragment(), CoroutineScope by MainScope() {
    private val posts: NewsApi = get()


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

            posts.getPosts().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    changeProgressState(false)
                }
                .subscribe({ postList ->
                    setList(list = postList.toMutableList())


                }, {

                    Toast.makeText(context, it.localizedMessage, Toast.LENGTH_LONG).show()
                })
                .addTo(compositeDisposable)
            //setList()

        } catch (e: Exception) {
            Toast.makeText(
                requireContext(), getString(R.string.request_internet_false),
                Toast.LENGTH_LONG
            ).show()
        }

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
