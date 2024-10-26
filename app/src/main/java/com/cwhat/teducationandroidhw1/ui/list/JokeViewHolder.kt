package com.cwhat.teducationandroidhw1.ui.list

import androidx.recyclerview.widget.RecyclerView
import com.cwhat.teducationandroidhw1.data.Joke
import com.cwhat.teducationandroidhw1.databinding.JokeItemBinding

class JokeViewHolder(private val binding: JokeItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(joke: Joke) {
        joke.run {
            binding.category.text = category
            binding.question.text = question
            binding.answer.text = answer
        }
    }

}