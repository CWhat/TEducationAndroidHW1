package com.cwhat.teducationandroidhw1.ui.list

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cwhat.teducationandroidhw1.R
import com.cwhat.teducationandroidhw1.data.JokesRepository
import com.cwhat.teducationandroidhw1.databinding.ActivityJokesBinding

class JokesListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJokesBinding

    private val adapter = JokesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityJokesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val jokesList = findViewById<RecyclerView>(R.id.jokes_list)
        val listPaddingTop = jokesList.paddingTop
        val listPaddingBottom = jokesList.paddingBottom
        ViewCompat.setOnApplyWindowInsetsListener(jokesList) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top + listPaddingTop,
                systemBars.right,
                systemBars.bottom + listPaddingBottom
            )
            insets
        }

        setupList()
        loadJokes()
    }

    private fun setupList() {
        with(binding.jokesList) {
            adapter = this@JokesListActivity.adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun loadJokes() {
        val repository = JokesRepository()
        val jokes = repository.getJokes()
        adapter.setData(jokes)
    }
}