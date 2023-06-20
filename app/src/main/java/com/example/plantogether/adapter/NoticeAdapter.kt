package com.example.plantogether.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.plantogether.data.NoticeData
import com.example.plantogether.databinding.RowNoticeBinding
import java.text.SimpleDateFormat
import java.util.Date

class NoticeAdapter(var items : ArrayList<NoticeData>): RecyclerView.Adapter<NoticeAdapter.ViewHolder>() {

    //이곳에 item중 초대장만 받아온다.
    val mFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

    interface OnItemClickListener {
        fun OnItemClick(position: Int)
    }
    var itemClickListener: NoticeAdapter.OnItemClickListener? = null


    inner class ViewHolder(val binding: RowNoticeBinding)
        : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val view = RowNoticeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val date = Date(items[position].time)
            val dateText = mFormat.format(date)

            rowNoticeName.setText(items[position].title)
            rowRemainTime.setText(dateText)
            rowNoticeInfo.setText(items[position].notice)
        }
    }
}