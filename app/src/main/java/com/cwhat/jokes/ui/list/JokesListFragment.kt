package com.cwhat.jokes.ui.list

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cwhat.jokes.App
import com.cwhat.jokes.R
import com.cwhat.jokes.databinding.FragmentJokesBinding
import com.cwhat.jokes.domain.entity.Joke
import com.cwhat.jokes.ui.ViewModelFactory
import com.cwhat.jokes.ui.list.recyclerview.JokesAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

class JokesListFragment : Fragment(R.layout.fragment_jokes) {
    private val binding: FragmentJokesBinding by viewBinding(FragmentJokesBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val jokesViewModel: JokesViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as App).appComponent.inject(this)
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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                jokesViewModel.errors.collect { error ->
                    val toast = when (error) {
                        is JokesError.ErrorWithString ->
                            Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG)

                        is JokesError.ErrorWithId ->
                            Toast.makeText(requireContext(), error.resId, Toast.LENGTH_LONG)
                    }
                    toast.show()
                }
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