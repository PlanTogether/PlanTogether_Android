package com.example.plantogether.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantogether.activity.EditEventActivity
import com.example.plantogether.activity.EventInfoActivity
import com.example.plantogether.activity.MakeEventActivity
import com.example.plantogether.adapter.DateViewAdapter
import com.example.plantogether.databinding.ActivityAddScheduleDialogBinding
import com.example.plantogether.dialog.data.EventData
import com.example.plantogether.roomDB.Event
import com.example.plantogether.roomDB.EventDatabase
import com.example.plantogether.roomDB.Plan
import com.kakao.sdk.common.KakaoSdk.init
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//날짜를 클릭하면 그 날에 저장되어있는 일정을 보여준다.
class AddScheduleDialogActivity(private val context : AppCompatActivity) {

    lateinit var binding : ActivityAddScheduleDialogBinding
    lateinit var db : EventDatabase

    var adapter = DateViewAdapter(ArrayList<Event>())
    var eventData = ArrayList<Event>()
    var planData = ArrayList<Plan>()
    var date = ""
    val dlg = Dialog(context)
    var i = 0
    fun show(todayDate : String) {

        binding = ActivityAddScheduleDialogBinding.inflate(context.layoutInflater)
        dlg.setContentView(binding.root)
        //바깥에 누르면 없어지게 만드는 기능
        dlg.setCancelable(true)

        date = todayDate

        initRecyclerView()
        initLayout(todayDate)

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
            }

            override fun OnDeleteItemClick(event: Event) {
                //삭제 버튼 눌렀을 때
                Toast.makeText(context, "삭제버튼 클릭.", Toast.LENGTH_SHORT).show()
                val title = event.title
                val builder = AlertDialog.Builder(context)
                builder.setTitle(title + " 삭제 여부")
                    .setMessage("이 일정을 삭제하시겠습니까?")
                    .setPositiveButton("확인",
                    DialogInterface.OnClickListener { _, _ ->
                        //이벤트 삭제
                        if(event.type == 1) {
                            CoroutineScope(Dispatchers.IO).launch {
                                delete(event)
                                getEvent()
                            }
                        }
                        //일정 삭제
                        else {
                            CoroutineScope(Dispatchers.IO).launch {
                                val plan = db.eventDao().getPlanById(event.id)
                                db.eventDao().deletePlan(plan)
                                getEvent()
                            }


                        }

                        Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    })
                    .setNegativeButton("취소", DialogInterface.OnClickListener{
                            _, _ ->

                    })
                    .create()

                val dialog = builder.create()
                dialog.show()
            }

            override fun OnItemClick3(event: Event) {
                if(event.type == 1) {
                    val intent = Intent(context, EditEventActivity::class.java)
                    intent.putExtra("id",event.id)
                    context.startActivity(intent)
                }

            }

        })
        adapter.notifyDataSetChanged()
        binding.eventRecyclerView.adapter= adapter
    }

    fun initLayout(dates : String) {
        db = EventDatabase.getDatabase(context)

        binding.initDate.setText(date)
        CoroutineScope(Dispatchers.IO).launch {
            getEvent()
        }


        //이벤트추가
        binding.plusEvent.setOnClickListener {

            /*
            //출력용
            val practice = Event(i, 1, "aa", "aa", date, "11:00", "")
            i++
            Toast.makeText(context, practice.id.toString(), Toast.LENGTH_SHORT).show()

            CoroutineScope(Dispatchers.IO).launch {
                db.eventDao().insertEvent(practice)
                getEvent()
            }*/


            val intent = Intent(context, MakeEventActivity::class.java)
            intent.putExtra("date",date)
            context.startActivity(intent)

        }

        //일정추가
        binding.plusPlan.setOnClickListener {
            val bottomSheetDialogFragment = AddPlanDialogActivity(date)
            bottomSheetDialogFragment.setItemClickListener(object : AddPlanDialogActivity.ItemClickListener{
                override fun changeData() {
                    CoroutineScope(Dispatchers.IO).launch {
                        getEvent()
                    }
                }
            })
            val intent = Intent(context, AddPlanDialogActivity::class.java)
            bottomSheetDialogFragment.show(context.supportFragmentManager, "bottom_sheet_dialog")

        }

    }

    fun getEvent() {
        eventData = db.eventDao().getEventByTitle(date) as ArrayList<Event>
        planData = db.eventDao().getPlanByTitle(date) as ArrayList<Plan>

        for ( x in planData) {
            val PlanToEvent = Event(x.id, 2, x.title, "", x.date, x.time, "")
            eventData.add(PlanToEvent)
        }

        adapter.items = eventData
        CoroutineScope(Dispatchers.Main).launch {
            adapter.notifyDataSetChanged()
        }
    }

    fun delete(event: Event) {
        db.eventDao().deleteEvent(event.id)
        getEvent()
    }
}