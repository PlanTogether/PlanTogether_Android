package com.example.plantogether.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plantogether.activity.EditEventActivity
import com.example.plantogether.adapter.EventDataAdapter
import com.example.plantogether.data.EventData
import com.example.plantogether.databinding.FragmentEventBinding
import com.example.plantogether.databinding.RowEventBinding
import com.example.plantogether.roomDB.Event
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
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

    var data: ArrayList<EventData> = ArrayList()
    val selected: ArrayList<Boolean> = ArrayList()

    lateinit var rdb: DatabaseReference
    var userName: String = ""
    var eventData = ArrayList<EventData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userName = arguments?.getString("userName").toString()
        initRecyclerView()
        // println("사용자명 : " + userName + " in EventFragment")
    }

    private fun initRecyclerView() {
        binding.recyclerViewEvent.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )

        val simpleCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                adapter.moveItem(viewHolder.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.removeItem(viewHolder.adapterPosition)
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewEvent)

        CoroutineScope(Dispatchers.IO).launch {
            getEventData()
            datasort()

            withContext(Dispatchers.Main) {
                adapter = EventDataAdapter(data, selected)
                adapter.itemClickListener = object : EventDataAdapter.OnItemClickListener {
                    override fun OnItemClick(
                        data: EventData,
                        binding: RowEventBinding,
                        position: Int
                    ) {
                        adapter.updateItemAtPosition(position, data)
                    }
                }
                adapter.onApplyClickListener = object : EventDataAdapter.OnApplyClickListener {
                    override fun onApplyClick(data: EventData) {
                        val intent = Intent(requireContext(), EditEventActivity::class.java)
                        intent.putExtra("id", data.id)
                        startActivity(intent)
                    }
                }
                binding.recyclerViewEvent.adapter = adapter
            }
        }
    }

    private fun datasort() {
        val sdf = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
        data.sortBy { sdf.parse(it.date) }
    }

    private fun getEventData() {
        rdb = Firebase.database.getReference("Events/items")
        val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                eventData.clear()

                for (childSnapshot in snapshot.children) {
                    val event = childSnapshot.getValue(EventData::class.java)
                    event?.let {
                        eventData.add(it)
                    }
                }

                adapter.items = eventData
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // 처리 실패 시 호출되는 메서드
            }
        }
        rdb.addValueEventListener(eventListener)
    }
}
