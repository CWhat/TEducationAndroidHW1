package com.cwhat.jokes.ui.list.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.cwhat.jokes.domain.entity.Joke
import com.cwhat.jokes.databinding.JokeItemBinding
import com.cwhat.jokes.ui.typeToString

class JokeViewHolder(private val binding: JokeItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(joke: Joke) {
        with(binding) {
            category.text = joke.category
            question.text = joke.question
            answer.text = joke.answer
            type.text = root.context.typeToString(joke.type)
        }
    }

}