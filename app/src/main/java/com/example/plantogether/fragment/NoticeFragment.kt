package com.example.plantogether.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantogether.activity.EditEventActivity
import com.example.plantogether.adapter.NoticeAdapter
import com.example.plantogether.databinding.FragmentNoticeBinding
import com.example.plantogether.roomDB.Event
import com.example.plantogether.roomDB.EventDatabase
import com.example.plantogether.roomDB.Notice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NoticeFragment : Fragment() {
    //오늘 기준 이후의 이벤트를 가져와서
    //오늘의 일정, 변경된 이벤트, 초대장 수락 거절등의 정보를 가져온다.

    lateinit var db : EventDatabase
    lateinit var binding : FragmentNoticeBinding
    lateinit var adapter : NoticeAdapter
    var data : ArrayList<Notice> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoticeBinding.inflate(layoutInflater, container, false)


        val today = LocalDate.now()
        var formatter = DateTimeFormatter.ofPattern("yyyy년 M월 dd일")
        val formattedDate = today.format(formatter)

        db = EventDatabase.getDatabase(this.requireContext())



        CoroutineScope(Dispatchers.IO).launch {
            data = db.eventDao().getNotice() as ArrayList<Notice>
            Log.d("asdf", formattedDate)
            val todayEvent = db.eventDao().getEventToday(formattedDate) as ArrayList<Event>
            for(a in todayEvent) {
                Log.d("asdf", "해당 당일 이벤트 pid : " + a)
                val todayNotice = Notice(0, a.id, a.title, LocalDate.now().toString(), a.date, 6)
                data.add(todayNotice)
            }

            withContext(Dispatchers.Main){
                initRecyclerView()
            }
        }
        return binding.root

    }

    private fun initRecyclerView() {
        binding.noticeRecyclerView.layoutManager = LinearLayoutManager(requireContext(),
        LinearLayoutManager.VERTICAL, false)

        adapter = NoticeAdapter(data)
        adapter.itemClickListener = object : NoticeAdapter.OnItemClickListener {
            override fun OnItemClick(position: Int) {
                when(data[position].type) {
                    1, 5, 6-> {
                        val eventID = data[position].pid
                        Log.d("gotoEvent", eventID.toString())
                        CoroutineScope(Dispatchers.IO).launch {
                            val find = db.eventDao().getEventById(eventID)
                            val intent = Intent(context, EditEventActivity::class.java)
                            intent.putExtra("id", eventID)
                            startActivity(intent)
                        }
                    }
                    2 -> {
                        //이벤트 초대장 받기 부분으로 들어가기
                    }
                    3 -> {

                    }
                }
            }

        }
        binding.noticeRecyclerView.adapter = adapter

    }

    /*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {


        }


    }*/
}