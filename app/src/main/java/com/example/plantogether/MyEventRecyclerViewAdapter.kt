package com.example.plantogether

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.plantogether.databinding.RowEventBinding

class MyEventRecyclerViewAdapter(private val items: ArrayList<MyEventData>): RecyclerView.Adapter<MyEventRecyclerViewAdapter.ViewHolder>() {

    //주석처리됨 onClickListener부분은 디버깅용코드

//    interface OnItemClickListener{
//        fun OnItemClick(data: MyEventData, position: Int)
//
//    }
//    var itemClickListener1: OnItemClickListener? =null
//    var itemClickListener2: OnItemClickListener? =null

    inner class ViewHolder(val binding: RowEventBinding): RecyclerView.ViewHolder(binding.root) {
        init{
//            binding.rowDate.setOnClickListener {
//                itemClickListener1?.OnItemClick(items[adapterPosition], adapterPosition)
//            }
//            binding.rowPlan.setOnClickListener {
//                itemClickListener2?.OnItemClick(items[adapterPosition], adapterPosition)
//            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.rowEventDate.text = items[position].date
        holder.binding.rowEventTitle.text = items[position].plan
    }
}