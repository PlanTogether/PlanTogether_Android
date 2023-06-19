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
    var adapter = EventDataAdapter(ArrayList<EventData>())

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
        getEventData()
        datasort()
        // println("사용자명 : " + userName + " in EventFragment")
    }

    private fun initRecyclerView() {
        binding.recyclerViewEvent.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )

        // adapter = EventDataAdapter(eventData, selected)

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

    private fun datasort() {
        val sdf = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
        eventData.sortBy { sdf.parse(it.date) }
        adapter.notifyItemChanged(eventData.size)
    }

    private fun getEventData() {
        println("사용자명 : " + userName)
        rdb = Firebase.database.getReference("$userName/Events")
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
        println("eventData 사이즈 : " + eventData.size)
        adapter.notifyItemInserted(eventData.size)
    }
}
