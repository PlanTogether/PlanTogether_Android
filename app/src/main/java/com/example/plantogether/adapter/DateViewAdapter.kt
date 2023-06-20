package com.example.plantogether.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.plantogether.databinding.RowAddEventBinding
import com.example.plantogether.data.EventData
import com.example.plantogether.databinding.RowAddPlanBinding

class DateViewAdapter(var items : ArrayList<EventData>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun OnItemClick(eventData: EventData, position: Int)
        fun OnDeleteItemClick(eventData: EventData, position: Int)
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
                itemClickListener?.OnItemClick(eventData, adapterPosition)
            }
        }
    }

    inner class PlanViewHolder(val binding: RowAddPlanBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(planData: EventData) {
            binding.planNameText.text = planData.title
            binding.planTimeText.text = planData.time

            binding.planItem.setOnClickListener {
                itemClickListener?.OnItemClick(planData, adapterPosition)
            }

            binding.showDelete.setOnClickListener {
                itemClickListener?.OnDeleteItemClick(planData, adapterPosition)
            }
        }
    }

    companion object {
        private const val TYPE_EVENT = 1
        private const val TYPE_PLAN = 2
    }
}
