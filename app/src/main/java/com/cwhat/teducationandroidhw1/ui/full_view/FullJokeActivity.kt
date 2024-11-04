package com.cwhat.teducationandroidhw1.ui.full_view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cwhat.teducationandroidhw1.R
import com.cwhat.teducationandroidhw1.data.Joke
import com.cwhat.teducationandroidhw1.databinding.ActivityFullJokeBinding

class FullJokeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFullJokeBinding
    private val fullJokeViewModel: FullJokeViewModel by viewModels()

    companion object {
        private const val JOKE_ID_EXTRA = "JOKE_ID"
        private const val INVALID_JOKE_ID = -1

        fun getInstance(context: Context, jokeId: Int): Intent {
            return Intent(context, FullJokeActivity::class.java).apply {
                putExtra(JOKE_ID_EXTRA, jokeId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFullJokeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupObservers()
        loadJoke()
    }

    private fun setupObservers() {
        fullJokeViewModel.joke.observe(this) {
            setPage(it)
        }
        fullJokeViewModel.error.observe(this) {
            handleError()
        }
    }

    private fun setPage(joke: Joke) {
        joke.run {
            binding.category.text = category
            binding.question.text = question
            binding.answer.text = answer
        }
    }

    private fun handleError() {
        Toast.makeText(this, "Invalid joke id", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun loadJoke() {
        val id = intent.getIntExtra(JOKE_ID_EXTRA, INVALID_JOKE_ID)
        fullJokeViewModel.loadJoke(id)
    }
}