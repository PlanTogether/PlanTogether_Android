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
import com.example.plantogether.activity.EventInfoActivity
import com.example.plantogether.adapter.EventDataAdapter
import com.example.plantogether.dialog.data.EventData
import com.example.plantogether.databinding.FragmentEventBinding
import com.example.plantogether.databinding.RowEventBinding
import com.example.plantogether.roomDB.Event
import com.example.plantogether.roomDB.EventDatabase
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
    lateinit var db : EventDatabase
    lateinit var event: Event
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventBinding.inflate(inflater, container, false)

        db = EventDatabase.getDatabase(this.requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            data = db.eventDao().getEvents() as ArrayList<Event>
            withContext(Dispatchers.Main){
                initData()
                initRecyclerView()
                /*
                adapter.onApplyClickListener = object : EventDataAdapter.OnApplyClickListener {
                    override fun onApplyClick(data: Event) {
                        val intent = Intent(requireContext(), EditEventActivity::class.java)
                        intent.putExtra("id", data.id)
                        startActivity(intent)

                    }
                }*/
                adapter.itemClickListener = object : EventDataAdapter.OnItemClickListener {
                    override fun OnItemClick(data: Event, binding: RowEventBinding, position: Int) {
                        val intent = Intent(requireContext(), EventInfoActivity::class.java)
                        intent.putExtra("id", data.id)
                        startActivity(intent)
                    }

                }

            }
        }



        return binding.root
    }

    fun initRecyclerView() {
        binding.recyclerViewEvent.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)
        adapter = EventDataAdapter(data, selected)
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