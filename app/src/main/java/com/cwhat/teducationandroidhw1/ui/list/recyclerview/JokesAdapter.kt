package com.cwhat.teducationandroidhw1.ui.list.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cwhat.teducationandroidhw1.R
import com.cwhat.teducationandroidhw1.domain.entity.Joke
import com.cwhat.teducationandroidhw1.domain.entity.JokeType
import com.cwhat.teducationandroidhw1.databinding.JokeItemBinding
import com.cwhat.teducationandroidhw1.databinding.LoadingItemBinding

class JokesAdapter(private val onItemClick: (id: Int, type: JokeType) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var data = emptyList<Joke>()

    var isLoading = false
        set(value) {
            if (field == value)
                return

            field = value
            if (value)
                notifyItemInserted(data.size)
            else
                notifyItemRemoved(data.size)
        }

    override fun getItemViewType(position: Int): Int {
        return if (isLoading && position == data.size)
            R.layout.loading_item
        else
            R.layout.joke_item
    }

    fun setData(newData: List<Joke>) {
        val callback = JokesDiffUtilCallback(data, newData)
        val calculatedDiff = DiffUtil.calculateDiff(callback)
        data = newData
        calculatedDiff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.joke_item -> {
                val binding = JokeItemBinding.inflate(inflater, parent, false)
                JokeViewHolder(binding).apply {
                    binding.root.setOnClickListener {
                        handleJokeClick(adapterPosition)
                    }
                }
            }

            R.layout.loading_item -> LoadingViewHolder(
                LoadingItemBinding.inflate(inflater, parent, false)
            )

            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun getItemCount(): Int = data.size + if (isLoading) 1 else 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isLoading && position == data.size)
            return

        if (holder !is JokeViewHolder)
            throw IllegalArgumentException("Unknown ViewHolder")

        holder.bind(data[position])
    }

    private fun handleJokeClick(position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val item = data[position]
            onItemClick(item.id, item.type)
        }
    }

}