package com.example.plantogether.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantogether.adapter.NoticeAdapter
import com.example.plantogether.data.NoticeData
import com.example.plantogether.databinding.FragmentNoticeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class NoticeFragment : Fragment() {
    //오늘 기준 이후의 이벤트를 가져와서
    //오늘의 일정, 변경된 이벤트, 초대장 수락 거절등의 정보를 가져온다.


    lateinit var binding : FragmentNoticeBinding
    lateinit var adapter : NoticeAdapter
    var data : ArrayList<NoticeData> = ArrayList()

    lateinit var noticedb: DatabaseReference
    var userName: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoticeBinding.inflate(layoutInflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userName = arguments?.getString("userName").toString()
        initRecyclerView()
        CoroutineScope(Dispatchers.IO).launch {
            initData()
        }
        // println("사용자명 : " + userName + " in EventFragment")
    }

    private fun initData() {
        noticedb = Firebase.database.getReference("$userName/Notices")
        val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                data.clear()

                for (childSnapshot in snapshot.children) {
                    val event = childSnapshot.getValue(NoticeData::class.java)
                    event?.let {
                          data.add(it)
                    }
                }

                adapter.items = data
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // 처리 실패 시 호출되는 메서드
            }
        }

        noticedb.addValueEventListener(eventListener)
    }
    private fun initRecyclerView() {

        binding.noticeRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        adapter = NoticeAdapter(data)
        adapter.itemClickListener = object : NoticeAdapter.OnItemClickListener {
            override fun OnItemClick(position: Int) {

            }

        }
        binding.noticeRecyclerView.adapter = adapter

    }
}