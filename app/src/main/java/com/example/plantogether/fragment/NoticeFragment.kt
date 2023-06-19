package com.example.plantogether.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.plantogether.activity.EditEventActivity
import com.example.plantogether.activity.EventInfoActivity
import com.example.plantogether.activity.MakeEventActivity
import com.example.plantogether.adapter.NoticeAdapter
import com.example.plantogether.databinding.FragmentNoticeBinding
import com.example.plantogether.roomDB.EventDatabase
import com.example.plantogether.roomDB.Notice
import com.example.plantogether.roomDB.Plan
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class NoticeFragment : Fragment() {
    //오늘 기준 이후의 이벤트를 가져와서
    //오늘의 일정, 변경된 이벤트, 초대장 수락 거절등의 정보를 가져온다.


    lateinit var binding : FragmentNoticeBinding
    lateinit var adapter : NoticeAdapter
    var data : ArrayList<Notice> = ArrayList()
    private var selectedDate: LocalDate?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoticeBinding.inflate(layoutInflater, container, false)
        val today = LocalDate.now().toString()
        Log.d("today", today)




        CoroutineScope(Dispatchers.IO).launch {
            for ( k in data) {
                Log.d("notices", k.id.toString() + " " + k.title + k.type.toString())
            }
            withContext(Dispatchers.Main){
                initData()
                initRecyclerView()
            }
        }
        return binding.root

    }

    private fun initData() {
        CoroutineScope(Dispatchers.IO).launch {

        }
    }
    private fun initRecyclerView() {
        binding.noticeRecyclerView.layoutManager = LinearLayoutManager(requireContext(),
        LinearLayoutManager.VERTICAL, false)
        adapter = NoticeAdapter(data)
        adapter.itemClickListener = object : NoticeAdapter.OnItemClickListener {
            override fun OnItemClick(position: Int) {
                when(data[position].type) {
                    1 or 5-> {
                        val eventID = data[position].pid
                        CoroutineScope(Dispatchers.IO).launch {
                            val intent = Intent(context, EditEventActivity::class.java)
                            intent.putExtra("id", eventID)
                            startActivity(intent)
                        }
                    }
                    2 -> {
                        //이벤트 초대장 받기 부분으로 들어가기
                    }
                    4 -> {

                    }

                }
            }

        }
        binding.noticeRecyclerView.adapter = adapter


    }
    private fun initLayout() {

    }
}