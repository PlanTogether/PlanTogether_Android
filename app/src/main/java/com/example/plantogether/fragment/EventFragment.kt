package com.example.plantogether.fragment

import android.content.Intent
import android.icu.text.Transliterator.Position
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plantogether.R
import com.example.plantogether.activity.EditEventActivity
import com.example.plantogether.adapter.EventDataAdapter
import com.example.plantogether.data.EventData
import com.example.plantogether.databinding.FragmentEventBinding
import com.example.plantogether.databinding.RowEventBinding
import com.example.plantogether.roomDB.Event
import com.example.plantogether.roomDB.EventDatabase
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EventFragment : Fragment() {
    lateinit var binding: FragmentEventBinding
    lateinit var adapter: EventDataAdapter

    var data:ArrayList<Event> = ArrayList()
    val selected:ArrayList<Boolean> = ArrayList()
    lateinit var event: EventData

    lateinit var rdb: DatabaseReference
    var userName: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventBinding.inflate(inflater, container, false)

        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main){
            //    initData()
                initRecyclerView()
                adapter.onApplyClickListener = object : EventDataAdapter.OnApplyClickListener {
                    override fun onApplyClick(data: Event) {
                        val intent = Intent(requireContext(), EditEventActivity::class.java)
                        intent.putExtra("id", data.id)
                        startActivity(intent)

                    }
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userName = arguments?.getString("userName").toString()
        // println("사용자명 : " + userName + " in EventFragment")
    }

    fun initRecyclerView() {
        binding.recyclerViewEvent.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)

        rdb = Firebase.database.getReference("Events/items")
        val query = rdb.limitToLast(50)
        val option = FirebaseRecyclerOptions.Builder<Event>()
            .setQuery(query, Event::class.java)
            .build()

        adapter = EventDataAdapter(option, data, selected)
        adapter.itemClickListener = object :EventDataAdapter.OnItemClickListener {
            override fun OnItemClick(data: Event, binding: RowEventBinding, position: Int) {
                adapter.updateItemAtPosition(position, data)
            }

        }
        binding.recyclerViewEvent.adapter = adapter
        val simpleCallback = object: ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                adapter.moveItem(viewHolder.adapterPosition, target.adapterPosition)//옮기는 옵션 빠른 시간순으로 정렬하면 필요 없을 것
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {//데이터 관련 내용은 DB에서 할것이니 굳이 건드릴 필요가 없을 것
                adapter.removeItem(viewHolder.adapterPosition)
            }

        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewEvent)
        Log.i("bind?","??")
    }
    fun initData() {

        for (i: Int in 0 until data.size) {
            selected.add(false)
        }
        datasort()
    }

    fun datasort(){
        val sdf = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
        data.sortBy { sdf.parse(it.date) }

    }
}