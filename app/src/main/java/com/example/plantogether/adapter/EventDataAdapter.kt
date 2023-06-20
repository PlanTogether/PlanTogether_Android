package com.example.plantogether.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.plantogether.data.EventData
import com.example.plantogether.databinding.RowEventBinding

class EventDataAdapter(var items: ArrayList<EventData>)
    : RecyclerView.Adapter<EventDataAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun OnItemClick(eventData: EventData, position: Int)

    }

    var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    inner class ViewHolder(val binding: RowEventBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(eventData : EventData) {

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fullDate = items[position].date
        val splitIndex = fullDate?.indexOf(" ")
        if (splitIndex != -1 && splitIndex!! < (fullDate?.length ?: 0) - 1) {
            val DateExceptYear =
                fullDate?.substring(splitIndex?.plus(1) ?: 0)
            holder.binding.rowEventDate.text = DateExceptYear
        } else {
            holder.binding.rowEventDate.text = fullDate
        }
        holder.binding.rowEventTitle.text = items[position].title

        holder.binding.rowEvent.setOnClickListener {
            itemClickListener?.OnItemClick(items[position], position)
        }
    }


    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItemAtPosition(position: Int, data: EventData) {
        items[position] = data
        notifyItemChanged(position)
    }
}