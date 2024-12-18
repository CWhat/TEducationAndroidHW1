package com.cwhat.jokes.ui.full_view

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
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cwhat.jokes.App
import com.cwhat.jokes.R
import com.cwhat.jokes.databinding.FragmentFullJokeBinding
import com.cwhat.jokes.domain.entity.Joke
import com.cwhat.jokes.ui.ViewModelFactory
import com.cwhat.jokes.ui.typeToString
import kotlinx.coroutines.launch
import javax.inject.Inject

class FullJokeFragment : Fragment(R.layout.fragment_full_joke) {
    private val binding: FragmentFullJokeBinding by viewBinding(FragmentFullJokeBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val fullJokeViewModel: FullJokeViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as App).appComponent.inject(this)
    }

    private val args: FullJokeFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupScreen()
        setupObservers()
        loadJoke()
    }

    private fun setupScreen() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom,
            )
            insets
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                fullJokeViewModel.joke.collect {
                    setPage(it)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                fullJokeViewModel.error.collect {
                    handleError()
                }
            }
        }
    }

    private fun setPage(joke: Joke) {
        joke.run {
            binding.category.text = category
            binding.question.text = question
            binding.answer.text = answer
            binding.type.text = requireContext().typeToString(type)
        }
    }

    private fun handleError() {
        activity?.let { Toast.makeText(it, "Invalid joke id", Toast.LENGTH_SHORT).show() }
        findNavController().popBackStack()
    }

    private fun loadJoke() {
        fullJokeViewModel.loadJoke(args.jokeId, args.jokeType)
    }

}