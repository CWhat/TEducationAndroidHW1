package com.cwhat.jokes.ui.add_joke

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cwhat.jokes.App
import com.cwhat.jokes.R
import com.cwhat.jokes.databinding.FragmentAddJokeBinding
import com.cwhat.jokes.ui.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddJokeFragment : Fragment(R.layout.fragment_add_joke) {
    private val binding: FragmentAddJokeBinding by viewBinding(FragmentAddJokeBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: AddJokeViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as App).appComponent.inject(this)
    }

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