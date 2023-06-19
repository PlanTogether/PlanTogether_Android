package com.example.plantogether.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.plantogether.activity.MainActivity
import com.example.plantogether.data.EventData
import com.example.plantogether.databinding.RowEventBinding
import com.example.plantogether.roomDB.Event
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class EventDataAdapter(var items: ArrayList<EventData>, val selected:ArrayList<Boolean>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
        init {
            binding.rowEvent.setOnClickListener {
                if (selected[adapterPosition] == false)
                    selected[adapterPosition] = true
                else if (selected[adapterPosition] == true)
                    selected[adapterPosition] = false
                itemClickListener?.OnItemClick(items[adapterPosition], binding, adapterPosition)
            }

            val datetext:String
            val numbers = items[position].date.split("[^\\d]+".toRegex())
            if(numbers.size >= 3) {

                val month = numbers[1]
                val day = numbers[2]
                datetext = "$month/$day"
            }else{
                datetext = "01/01"
            }
            binding.rowEventDate.text = datetext
            // 현재 Date에 들어갈 게 꽤 긴 Date내용이라 자르고 넣어야할 필요가 있다.
            binding.rowEventTitle.text = items[position].title
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItemAtPosition(position: Int, data: EventData) {
        items[position] = data
        notifyItemChanged(position)
    }
}