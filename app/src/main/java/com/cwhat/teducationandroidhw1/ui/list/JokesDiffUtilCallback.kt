package com.cwhat.teducationandroidhw1.ui.list

import androidx.recyclerview.widget.DiffUtil
import com.cwhat.teducationandroidhw1.data.Joke

class JokesDiffUtilCallback(
    private val oldData: List<Joke>,
    private val newData: List<Joke>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldData.size

    override fun getNewListSize(): Int = newData.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldData[oldItemPosition].id == newData[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldData[oldItemPosition] == newData[newItemPosition]

}