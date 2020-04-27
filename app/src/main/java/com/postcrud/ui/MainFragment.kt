package com.postcrud.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager

import com.postcrud.R
import com.postcrud.client.Api
import com.postcrud.data.DataServer
import com.postcrud.data.Post
import com.postcrud.data.adapters.PostRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.*

class MainFragment : Fragment(), CoroutineScope by MainScope() {


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

    private fun setList(list: MutableList<Post>) = launch {
        withContext(Dispatchers.Main) {
            with(recyclerListPosts) {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = PostRecyclerAdapter(list)
            }
            Api.client.close()
        }
    }

    private fun fetchData() = launch {
        try {
            setList(list = DataServer.getData())
            indeterminateBar.visibility = View.GONE
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(), getString(R.string.request_internet_false),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}