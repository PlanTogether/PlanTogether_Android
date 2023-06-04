package com.example.plantogether.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.plantogether.activity.MainActivity
import com.example.plantogether.dialog.data.EventData
import com.example.plantogether.databinding.RowEventBinding

class EventDataAdapter (val items:ArrayList<EventData>, val selected:ArrayList<Boolean>)
    : RecyclerView.Adapter<EventDataAdapter.ViewHolder>() {
    var onApplyClickListener: OnApplyClickListener? = null
    interface OnItemClickListener {
        fun OnItemClick(data: EventData, binding: RowEventBinding, position: Int)
    }

    interface OnApplyClickListener {
        fun onApplyClick(data: EventData)
    }

    var itemClickListener: OnItemClickListener? = null

    inner class ViewHolder(val binding: RowEventBinding)
        : RecyclerView.ViewHolder(binding.root) {
        private val mainActivity: MainActivity? = binding.root.context as? MainActivity
        init {
            binding.rowEvent.setOnClickListener {
                if (selected[adapterPosition] == false)
                    selected[adapterPosition] = true
                else if (selected[adapterPosition] == true)
                    selected[adapterPosition] = false
                itemClickListener?.OnItemClick(items[adapterPosition], binding, adapterPosition)
            }
        }
    }

    fun moveItem(oldPos: Int, newPos: Int) {//필요 없는 내용
        val data = items.get(oldPos)
        items[oldPos] = items.get(newPos)
        items[newPos] = data
        notifyItemMoved(oldPos, newPos)
    }

    fun removeItem(pos: Int) {
        items.removeAt(pos)
        notifyItemRemoved(pos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.rowEventDate.text = items[position].date// 현재 Date에 들어갈 게 꽤 긴 Date내용이라 자르고 넣어야할 필요가 있다.
        holder.binding.rowEventTitle.text = items[position].title

    }

    fun updateItemAtPosition(position: Int, data: EventData) {
        items[position] = data
        notifyItemChanged(position)
    }
}