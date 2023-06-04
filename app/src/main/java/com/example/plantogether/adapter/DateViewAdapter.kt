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

    var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_EVENT -> {
                val binding = RowAddEventBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                EventViewHolder(binding)
            }

            TYPE_PLAN -> {
                val binding = RowAddPlanBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                PlanViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EventViewHolder -> {
                val eventHolder = holder as EventViewHolder
                eventHolder.bind(items[position] as EventData)
            }

            is PlanViewHolder -> {
                val planHolder = holder as PlanViewHolder
                planHolder.bind(items[position] as EventData)
            }
        }
    }

    inner class EventViewHolder(val binding: RowAddEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(eventData: EventData) {
            binding.eventNameText.text = eventData.title

            binding.eventItem.setOnClickListener {
                itemClickListener?.OnItemClick(adapterPosition)
            }
        }
    }

    inner class PlanViewHolder(val binding: RowAddPlanBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(planData: EventData) {
            binding.planNameText.text = planData.title

            binding.planItem.setOnClickListener {
                itemClickListener?.OnItemClick(adapterPosition)
            }

            binding.showDelete.setOnClickListener {
                itemClickListener?.OnDeleteItemClick(adapterPosition)
            }
        }
    }

    companion object {
        private const val TYPE_EVENT = 1
        private const val TYPE_PLAN = 2
    }
}
