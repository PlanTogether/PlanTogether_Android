package com.example.plantogether.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plantogether.R
import com.example.plantogether.adapter.EventDataAdapter
import com.example.plantogether.data.EventData
import com.example.plantogether.databinding.FragmentEventBinding
import com.example.plantogether.databinding.RowEventBinding

class EventFragment : Fragment() {
    lateinit var binding: FragmentEventBinding
    lateinit var adapter: EventDataAdapter

    val data:ArrayList<EventData> = ArrayList()
    val selected:ArrayList<Boolean> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventBinding.inflate(inflater, container, false)
        initRecyclerView()
        adapter.onApplyClickListener = object : EventDataAdapter.OnApplyClickListener {
            override fun onApplyClick(data: EventData) {

            }
        }
        initData()
        return binding.root
    }

    fun initRecyclerView() {
        binding.recyclerViewEvent.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)
        adapter = EventDataAdapter(data, selected)
        adapter.itemClickListener = object :EventDataAdapter.OnItemClickListener {
            override fun OnItemClick(data: EventData, binding: RowEventBinding, position: Int) {
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
    }
    fun initData() {
        data.add(EventData(title = "신공 1013호 해커톤"))
        data.add(EventData(title = "홍길동 생일파티"))

        for (i: Int in 0 until data.size) {
            selected.add(false)
        }
    }
}