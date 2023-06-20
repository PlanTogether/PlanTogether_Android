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

class EventDataAdapter(var items: ArrayList<EventData>)
    : RecyclerView.Adapter<EventDataAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun OnItemClick(data: EventData, binding: RowEventBinding, position: Int)

        fun onApplyClick(data: EventData)
    }

    var itemClickListener: OnItemClickListener? = null

    inner class ViewHolder(val binding: RowEventBinding)
        : RecyclerView.ViewHolder(binding.root) {
        init {

            /*val datetext:String
            val numbers = items[position].date.split("[^\\d]+".toRegex())
            if(numbers.size >= 3) {

                val month = numbers[1]
                val day = numbers[2]
                datetext = "$month/$day"
            }else{
                datetext = "01/01"
            }*/
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
    }


    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItemAtPosition(position: Int, data: EventData) {
        items[position] = data
        notifyItemChanged(position)
    }
}