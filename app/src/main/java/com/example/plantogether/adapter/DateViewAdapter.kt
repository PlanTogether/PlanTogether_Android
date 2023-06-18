package com.example.plantogether.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.plantogether.databinding.RowAddEventBinding
import com.example.plantogether.dialog.data.EventData
import com.example.plantogether.databinding.RowAddPlanBinding
import com.example.plantogether.roomDB.Event
import com.example.plantogether.roomDB.Plan

class DateViewAdapter(var items : ArrayList<Event>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun OnItemClick(event: Event)
        fun OnDeleteItemClick(event: Event)


        fun OnItemClick3(event: Event)
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
                eventHolder.bind(items[position] as Event)
            }

            is PlanViewHolder -> {
                val planHolder = holder as PlanViewHolder
                planHolder.bind(items[position] as Event)
            }
        }

    }

    inner class EventViewHolder(val binding: RowAddEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(eventData: Event) {
            binding.eventNameText.text = eventData.title

            //event일떄만 수정 가능하게
            binding.eventItem.setOnClickListener {
                    itemClickListener?.OnItemClick(items[adapterPosition])
            }

            binding.showEventDelete.setOnClickListener {
                itemClickListener?.OnDeleteItemClick(eventData)
            }

            binding.eventItem.setOnClickListener {
                itemClickListener?.OnItemClick3(eventData)
            }

        }
    }

    inner class PlanViewHolder(val binding: RowAddPlanBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(planData: Event) {
            binding.planNameText.text = planData.title

            binding.planItem.setOnClickListener {
                itemClickListener?.OnItemClick(items[adapterPosition])
            }

            binding.showDelete.setOnClickListener {
                itemClickListener?.OnDeleteItemClick(items[adapterPosition])
            }
            binding.planTimeText.setText(planData.time)
        }
    }

    companion object {
        private const val TYPE_EVENT = 1
        private const val TYPE_PLAN = 2
    }
}
