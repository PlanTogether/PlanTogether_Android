package com.example.plantogether

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plantogether.databinding.FragmentEventBinding
import java.io.FileDescriptor
import java.io.PrintWriter


class EventFragment : Fragment() {
    private var binding: FragmentEventBinding? = null
    private var adapter: MyEventRecyclerViewAdapter? = null
    val columnCount = 1
    val eventDataArray = ArrayList<MyEventData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun initData() {
        //
        eventDataArray.add(MyEventData("05/25","추가된 일정"))

    }

    fun initRecyclerView(){//수정필요
        //현재 EventView의 투 텍스트에 디버깅용 코드 주석처리됨
        binding!!.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = MyEventRecyclerViewAdapter(eventDataArray)
//        adapter!!.itemClickListener1 = object :MyEventRecyclerViewAdapter.OnItemClickListener{
//            override fun OnItemClick(data: MyEventData, position: Int) {
//                val intent = Intent(context, EventInfoActivity::class.java)
//                startActivity(intent)
//            }
//
//        }
//        adapter!!.itemClickListener2 = object :MyEventRecyclerViewAdapter.OnItemClickListener{
//            override fun OnItemClick(data: MyEventData, position: Int) {
//                val intent = Intent(context, MakeEventActivity::class.java)
//                startActivity(intent)
//            }
//
//        }
        binding!!.recyclerView.adapter = adapter


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initRecyclerView()

    }

    private fun buttoninit() {//debuging
        binding.apply {

        }
    }


}