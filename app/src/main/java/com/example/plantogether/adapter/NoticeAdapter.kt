package com.example.plantogether.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.plantogether.databinding.RowNoticeBinding
import com.example.plantogether.roomDB.Event

class NoticeAdapter(var items : ArrayList<Event>): RecyclerView.Adapter<NoticeAdapter.ViewHolder>() {

    //이곳에 item중 초대장만 받아온다.

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
            rowNoticeName.setText(items[position].title)
            //요청을 수락하지 않은 경우 EventInfoActivity + 수락 거절 화면을 띄운다.
            if(items[position].type == 0) {
                rowNoticeInfo.setText("abcd")
            }
            //수락한 경우 정보만 표시한다.
            else {
                rowNoticeInfo.setText("이벤트가 저장되었습니다.")
            }
            noticeEvent.setOnClickListener {
                //수락 거절 다이얼로그 프레그먼트에서 띄우기
                itemClickListener?.OnItemClick(position)
            }


        }

    }
}