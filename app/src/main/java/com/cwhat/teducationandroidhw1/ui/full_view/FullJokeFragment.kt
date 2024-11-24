package com.cwhat.teducationandroidhw1.ui.full_view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cwhat.teducationandroidhw1.R
import com.cwhat.teducationandroidhw1.data.Joke
import com.cwhat.teducationandroidhw1.databinding.FragmentFullJokeBinding
import com.cwhat.teducationandroidhw1.ui.jokesViewModels
import com.cwhat.teducationandroidhw1.ui.typeToString

class FullJokeFragment : Fragment(R.layout.fragment_full_joke) {
    private val binding: FragmentFullJokeBinding by viewBinding(FragmentFullJokeBinding::bind)
    private val fullJokeViewModel: FullJokeViewModel by jokesViewModels { FullJokeViewModel(it) }
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
        fullJokeViewModel.joke.observe(viewLifecycleOwner) {
            setPage(it)
        }
        fullJokeViewModel.error.observe(viewLifecycleOwner) {
            handleError()
        }
    }

    private fun setPage(joke: Joke) {
        joke.run {
            binding.category.text = category
            binding.question.text = question
            binding.answer.text = answer
            context
            binding.type.text = requireContext().typeToString(type)
        }
    }

    private fun handleError() {
        activity?.let { Toast.makeText(it, "Invalid joke id", Toast.LENGTH_SHORT).show() }
        findNavController().popBackStack()
    }

    private fun loadJoke() {
        fullJokeViewModel.loadJoke(args.jokeId)
    }

}