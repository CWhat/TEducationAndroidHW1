package com.cwhat.teducationandroidhw1.ui.full_view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cwhat.teducationandroidhw1.R
import com.cwhat.teducationandroidhw1.data.Joke
import com.cwhat.teducationandroidhw1.data.JokesRepository
import com.cwhat.teducationandroidhw1.databinding.ActivityFullJokeBinding

class FullJokeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFullJokeBinding

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
        loadJoke()
    }

    private fun setupPage(joke: Joke) {
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
        val repository = JokesRepository()
        val id = intent.getIntExtra(JOKE_ID_EXTRA, INVALID_JOKE_ID)
        if (id == INVALID_JOKE_ID) {
            handleError()
        } else {
            val joke = repository.getJokeById(id)
            setupPage(joke)
        }
    }
}