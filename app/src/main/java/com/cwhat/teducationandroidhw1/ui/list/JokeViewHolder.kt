package com.cwhat.teducationandroidhw1.ui.list

import androidx.recyclerview.widget.RecyclerView
import com.cwhat.teducationandroidhw1.data.Joke
import com.cwhat.teducationandroidhw1.databinding.JokeItemBinding
import com.cwhat.teducationandroidhw1.ui.typeToString

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