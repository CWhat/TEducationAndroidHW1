package com.cwhat.teducationandroidhw1.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cwhat.teducationandroidhw1.data.Joke
import com.cwhat.teducationandroidhw1.databinding.JokeItemBinding

class JokesAdapter(private val onItemClick: (id: Int) -> Unit) :
    RecyclerView.Adapter<JokeViewHolder>() {

    private var data = emptyList<Joke>()

    fun setData(newData: List<Joke>) {
        val callback = JokesDiffUtilCallback(data, newData)
        val calculatedDiff = DiffUtil.calculateDiff(callback)
        data = newData
        calculatedDiff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = JokeItemBinding.inflate(inflater, parent, false)
        return JokeViewHolder(binding).apply {
            binding.root.setOnClickListener {
                handleJokeClick(adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: JokeViewHolder, position: Int) {
        holder.bind(data[position])
    }

    private fun handleJokeClick(position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val item = data[position]
            onItemClick(item.id)
        }
    }

}