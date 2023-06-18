package com.example.plantogether.dialog

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantogether.activity.EventInfoActivity
import com.example.plantogether.activity.MakeEventActivity
import com.example.plantogether.adapter.DateViewAdapter
import com.example.plantogether.databinding.ActivityAddScheduleDialogBinding
import com.example.plantogether.roomDB.Event
import com.example.plantogether.roomDB.EventDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddScheduleDialogActivity(private val context : AppCompatActivity) {

    lateinit var binding : ActivityAddScheduleDialogBinding

    lateinit var db : EventDatabase

    var adapter = DateViewAdapter(ArrayList<Event>())
    var eventData = ArrayList<Event>()

    var date = ""
    val dlg = Dialog(context)

    lateinit var rdb: DatabaseReference
    var userName: String = ""

    fun show(todayDate : String) {
        binding = ActivityAddScheduleDialogBinding.inflate(context.layoutInflater)
        dlg.setContentView(binding.root)
        //바깥에 누르면 없어지게 만드는 기능
        dlg.setCancelable(true)

        date = todayDate

        initRecyclerView()
        initLayout()

        val window = dlg.window
        val layoutParams = window?.attributes
        layoutParams?.width = (context.resources.displayMetrics.widthPixels * 0.7).toInt()
        layoutParams?.height = (context.resources.displayMetrics.heightPixels * 0.7).toInt()
        window?.attributes = layoutParams
        dlg.show()
    }
    fun initRecyclerView() {

        binding.eventRecyclerView.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL, false)

        adapter.setOnItemClickListener(object : DateViewAdapter.OnItemClickListener {
            override fun OnItemClick(event: Event) {
                //이곳에 이벤트 클릭했을 때 나오는거 쓰면됨.
                val intent = Intent(context, EventInfoActivity::class.java)
                intent.putExtra("userName", userName)
                intent.putExtra("id",event.id)
                context.startActivity(intent)
            }

            override fun OnDeleteItemClick(event: Event) {
                //삭제 버튼 눌렀을 때
                CoroutineScope(Dispatchers.IO).launch {
                    delete(event)
                }
            }

        })

        binding.eventRecyclerView.adapter = adapter
    }

    fun initLayout() {
        // println("사용자명 : " + userName + " in AddScheduleDialogActivity")
        db = EventDatabase.getDatabase(context)
        CoroutineScope(Dispatchers.IO).launch {
            getEvent()
        }
        binding.initDate.setText(date)

        //일정 추가
        binding.plusPlan.setOnClickListener {
            val bottomSheetDialogFragment = AddPlanDialogActivity()
            bottomSheetDialogFragment.setUserNameInPlan(userName)
            bottomSheetDialogFragment.setDateInPlan(date)
            bottomSheetDialogFragment.show(context.supportFragmentManager, "bottom_sheet_dialog")
        }
        //이벤트 추가
        binding.plusEvent.setOnClickListener {
            val intent = Intent(context, MakeEventActivity::class.java)
            intent.putExtra("userName", userName)
            intent.putExtra("date",date)
            context.startActivity(intent)

        }
    }

    fun getEvent() {
        rdb = Firebase.database.getReference("$userName/Events")
        val query = rdb.orderByChild("date").equalTo(date)
        CoroutineScope(Dispatchers.Main).launch {
            adapter.notifyDataSetChanged()
        }

        rdb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                eventData.clear()

                for (childSnapshot in snapshot.children) {
                    val event = childSnapshot.getValue(Event::class.java)
                    event?.let {
                        eventData.add(it)
                        println("Child 데이터 하나씩 가져오는 중")
                    }
                }

                adapter.items = eventData
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // 처리 실패 시 호출되는 메서드
            }
        })

        // eventData = db.eventDao().getEventByTitle(date) as ArrayList<Event>
        // adapter.items = eventData
    }

    fun delete(event: Event) {
        rdb = Firebase.database.getReference("$userName/Events")
        rdb.child(event.title).removeValue() // 클릭한 이벤트의 이벤트명을 키값으로 가진 녀석 제거
        db.eventDao().deleteEvent(event)
        getEvent()
    }

    fun setUserNameInScheduleDialog(userName: String) {
        this.userName = userName
    }
}