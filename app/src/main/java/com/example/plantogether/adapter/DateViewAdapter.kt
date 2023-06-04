package com.example.plantogether.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.plantogether.dialog.data.EventData
import com.example.plantogether.databinding.RowAddPlanBinding
import com.example.plantogether.roomDB.Event

class DateViewAdapter(var items : ArrayList<Event>): RecyclerView.Adapter<DateViewAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: RowAddPlanBinding) :
        RecyclerView.ViewHolder(binding.root){}

    interface OnItemClickListener {
        fun OnItemClick(position: Int)
        fun OnDeleteItemClick(position: Int)
    }
    var itemClickListener: OnItemClickListener ?= null
    fun setOnItemClickListener(listener : OnItemClickListener) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowAddPlanBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.planNameText.setText(items[position].title)


        holder.binding.eventList.setOnClickListener {
            itemClickListener?.OnItemClick(position)
        }
        holder.binding.showDelete.setOnClickListener {
            itemClickListener?.OnDeleteItemClick(position)
        }
    }

}