package com.example.plantogether.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.plantogether.databinding.RowNoticeBinding
import com.example.plantogether.roomDB.Event
import com.example.plantogether.roomDB.Notice
import com.example.plantogether.roomDB.Plan
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

class NoticeAdapter(var items : ArrayList<Notice>): RecyclerView.Adapter<NoticeAdapter.ViewHolder>() {

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


            //type 0 : plan새로 생성, 1 : event 새로 생성
            //2: 이벤트 초대장 도착 내용, 3 : 초대 수락 or 거절, 4 : 플랜 변경 내역 5 : 이벤트 변경 내역

            when(items[position].type) {
                0 or 4 -> {
                    val date = LocalDate.now()
                    Log.d("today plan1", date.toString())
                    val dateTimeStr = items[position].date
                    val dateStr = LocalDate.parse(dateTimeStr.substringBefore(" "))
                    val diff = ChronoUnit.DAYS.between(dateStr, date)
                    val days = Math.abs(diff.toInt())
                    Log.d("today plan12", "gap : " + days.toString())
                    var showday = ""

                    if (days == 0) {
                        showday = "오늘"
                        rowRemainTime.setText(showday)
                    } else
                        rowRemainTime.setText(Math.abs(diff.toInt()).toString() + "일 전")

                }
                else -> {
                    val date = LocalDate.now()
                    val dateStr = items[position].date
                    var todayDate = LocalDate.parse(dateStr)
                    val diff = ChronoUnit.DAYS.between(todayDate, date)
                    val days = Math.abs(diff.toInt())

                    var showday = ""
                    if (days == 0) {
                        showday = "오늘"
                        rowRemainTime.setText(showday)
                    } else
                        rowRemainTime.setText(Math.abs(diff.toInt()).toString() + "일 전")

                }
            }
            rowNoticeName.setText(items[position].title)
            //요청을 수락하지 않은 경우 EventInfoActivity + 수락 거절 화면을 띄운다.
            when(items[position].type) {
                0 -> rowNoticeInfo.setText("해당 일정이 추가되었습니다.")
                1 -> rowNoticeInfo.setText("해당 이벤트가 추가되었습니다.")
                2 -> rowNoticeInfo.setText("이벤트 초대장이 도착했습니다.")
                3 -> rowNoticeInfo.setText("name" + " 님이 초대장을 xx하였습니다.")
                4 -> rowNoticeInfo.setText("플랜 정보가 변경되었습니다.")
                5 -> rowNoticeInfo.setText("이벤트 정보가 변경되었습니다.")
            }
            //noticeEvent.setOnClickListener {
            //    //수락 거절 다이얼로그 프레그먼트에서 띄우기
            //    itemClickListener?.OnItemClick(position)
            //}


        }

    }
}