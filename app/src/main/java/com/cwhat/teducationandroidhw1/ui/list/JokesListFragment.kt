package com.cwhat.teducationandroidhw1.ui.list

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cwhat.teducationandroidhw1.R
import com.cwhat.teducationandroidhw1.databinding.FragmentJokesBinding
import com.cwhat.teducationandroidhw1.ui.jokesViewModels

class JokesListFragment : Fragment(R.layout.fragment_jokes) {
    private val binding: FragmentJokesBinding by viewBinding(FragmentJokesBinding::bind)
    private val jokesViewModel: JokesViewModel by jokesViewModels { JokesViewModel(it) }

    private val adapter = JokesAdapter { id ->
        val action = JokesListFragmentDirections.actionJokesListFragmentToFullJokeFragment(id)
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        setupFabOnClickListener()
        jokesViewModel.loadJokes()
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
        jokesViewModel.jokesList.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }
    }

    private fun setupFabOnClickListener() {
        binding.fabAddJoke.setOnClickListener {
            val action = JokesListFragmentDirections.actionJokesListFragmentToAddJokeFragment()
            findNavController().navigate(action)
        }
    }

}