package com.cwhat.teducationandroidhw1.ui.add_joke

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cwhat.teducationandroidhw1.R
import com.cwhat.teducationandroidhw1.databinding.FragmentAddJokeBinding
import com.cwhat.teducationandroidhw1.ui.jokesViewModels
import kotlinx.coroutines.launch

class AddJokeFragment : Fragment(R.layout.fragment_add_joke) {
    private val binding: FragmentAddJokeBinding by viewBinding(FragmentAddJokeBinding::bind)
    private val viewModel: AddJokeViewModel by jokesViewModels { AddJokeViewModel(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPaddings()
        setupListeners()
        observeEvents()
    }

    private fun setupPaddings() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
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

    private fun setupListeners() {
        with(binding) {
            btnAdd.setOnClickListener {
                val categoryText = category.text.toString()
                val questionText = question.text.toString()
                val answerText = answer.text.toString()
                viewModel.addJoke(categoryText, questionText, answerText)
            }
            btnCancel.setOnClickListener {
                navigateToBack()
            }
        }
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            viewModel.events.collect { event ->
                when (event) {
                    is AddJokeEvent.NavigateToBack -> navigateToBack()
                    is AddJokeEvent.ShowMessage -> showMessage(event.resId)
                }
            }
        }
    }

    private fun navigateToBack() {
        findNavController().popBackStack()
    }

    private fun showMessage(@StringRes resId: Int) {
        context?.let {
            Toast.makeText(it, resId, Toast.LENGTH_LONG).show()
        }
    }

}