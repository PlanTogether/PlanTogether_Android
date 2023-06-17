package com.example.plantogether.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.plantogether.R
import com.example.plantogether.adapter.EventDataAdapter
import com.example.plantogether.adapter.NoticeAdapter
import com.example.plantogether.databinding.FragmentNoticeBinding
import com.example.plantogether.roomDB.Event
import com.example.plantogether.roomDB.EventDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class NoticeFragment : Fragment() {
    //오늘 기준 이후의 이벤트를 가져와서
    //오늘의 일정, 변경된 이벤트, 초대장 수락 거절등의 정보를 가져온다.


    lateinit var db : EventDatabase
    var binding : FragmentNoticeBinding ?= null
    lateinit var adapter : NoticeAdapter
    var data : ArrayList<Event> = ArrayList()

    private var selectedDate: LocalDate?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoticeBinding.inflate(layoutInflater, container, false)

        db = EventDatabase.getDatabase(this.requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            data = db.eventDao().getEvents() as ArrayList<Event>
        }
        initLayout()



        return binding!!.root

    }

    private fun initLayout() {
        adapter = NoticeAdapter(data)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding!!.apply {


        }


    }
}