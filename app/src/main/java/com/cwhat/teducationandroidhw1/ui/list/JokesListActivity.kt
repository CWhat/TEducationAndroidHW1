package com.cwhat.teducationandroidhw1.ui.list

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
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