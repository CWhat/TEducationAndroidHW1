package com.cwhat.teducationandroidhw1.ui.list

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cwhat.teducationandroidhw1.R
import com.cwhat.teducationandroidhw1.data.Joke
import com.cwhat.teducationandroidhw1.data.di.DI
import com.cwhat.teducationandroidhw1.databinding.FragmentJokesBinding
import com.cwhat.teducationandroidhw1.ui.jokesViewModels
import kotlinx.coroutines.launch

class JokesListFragment : Fragment(R.layout.fragment_jokes) {
    private val binding: FragmentJokesBinding by viewBinding(FragmentJokesBinding::bind)
    private val jokesViewModel: JokesViewModel by jokesViewModels {
        JokesViewModel(
            it,
            DI.provideRemoteApi(),
        )
    }

    private val adapter = JokesAdapter { id, type ->
        val action = JokesListFragmentDirections.actionJokesListFragmentToFullJokeFragment(id, type)
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        setupFabOnClickListener()
    }

    private fun setOnScrollListener() {
        binding.jokesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy <= 0) return
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()

                val endHasBeenReached = lastVisible + 1 >= totalItemCount
                if (totalItemCount > 0 && endHasBeenReached)
                    jokesViewModel.loadJokes()
            }
        })
    }

    private fun setupList() {
        with(binding.jokesList) {
            adapter = this@JokesListFragment.adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            val listPaddingTop = paddingTop
            val listPaddingBottom = paddingBottom
            ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(
                    systemBars.left,
                    systemBars.top + listPaddingTop,
                    systemBars.right,
                    systemBars.bottom + listPaddingBottom,
                )
                insets
            }
        }
        observeState()
        setOnScrollListener()
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                jokesViewModel.state.collect { state ->
                    when (state) {
                        is JokesState.Loading -> setLoadingState()
                        is JokesState.EmptyList -> setEmptyListState()
                        is JokesState.ShowJokes -> setJokesListState(state.jokes)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                jokesViewModel.isLoading.collect { adapter.isLoading = it }
            }
        }
    }

    private fun setLoadingState() {
        with(binding) {
            loading.visibility = View.VISIBLE
            jokesList.visibility = View.GONE
            emptyListMessage.visibility = View.GONE
        }
    }

    private fun setEmptyListState() {
        with(binding) {
            loading.visibility = View.GONE
            jokesList.visibility = View.GONE
            emptyListMessage.visibility = View.VISIBLE
        }
    }

    private fun setJokesListState(data: List<Joke>) {
        with(binding) {
            loading.visibility = View.GONE
            jokesList.visibility = View.VISIBLE
            emptyListMessage.visibility = View.GONE
        }
        adapter.setData(data)
    }

    private fun setupFabOnClickListener() {
        binding.fabAddJoke.setOnClickListener {
            val action = JokesListFragmentDirections.actionJokesListFragmentToAddJokeFragment()
            findNavController().navigate(action)
        }
    }

}