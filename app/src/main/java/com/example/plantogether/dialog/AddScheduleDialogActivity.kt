package com.example.plantogether.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantogether.activity.EventInfoActivity
import com.example.plantogether.activity.MakeEventActivity
import com.example.plantogether.adapter.DateViewAdapter
import com.example.plantogether.databinding.ActivityAddScheduleDialogBinding
import com.example.plantogether.dialog.data.EventData
import com.example.plantogether.roomDB.Event
import com.example.plantogether.roomDB.EventDatabase
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

    var date = ""
    val dlg = Dialog(context)

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
                //이곳에 이벤트 클릭했을 때 나오는거 쓰면됨.
                val intent = Intent(context, EventInfoActivity::class.java)
                intent.putExtra("id",event.id)
                context.startActivity(intent)
            }

            override fun OnDeleteItemClick(event: Event) {
                //삭제 버튼 눌렀을 때
                val title = event.title
                val builder = AlertDialog.Builder(context)
                builder.setTitle(title + " 삭제 여부")
                    .setMessage("이 일정을 삭제하시겠습니까?")
                    .setPositiveButton("확인",
                    DialogInterface.OnClickListener { _, _ ->
                        CoroutineScope(Dispatchers.IO).launch {
                            delete(event)
                        }
                        Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    })
                    .setNegativeButton("취소", DialogInterface.OnClickListener{
                            _, _ ->

                    })
                    .create()
            }

        })

        binding.eventRecyclerView.adapter= adapter
    }

    fun initLayout(dates : String) {
        db = EventDatabase.getDatabase(context)
        CoroutineScope(Dispatchers.IO).launch {
            getEvent()
        }
        binding.initDate.setText(date)


        /*
        //이벤트 제대로 되는 지 출력...? 안됨
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("events", "모든 이벤트 출력하기")
            var kk = db.eventDao().getEvents() as ArrayList<Event>

            for(x in kk) {
                var a1 = x.title
                Log.d("events", a1)
            }
            Log.d("events", "=======================")
        }*/



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
        //이벤트추가
        binding.plusEvent.setOnClickListener {
            val intent = Intent(context, MakeEventActivity::class.java)
            intent.putExtra("date",date)
            context.startActivity(intent)

        }



    }

    fun getEvent() {
        eventData = db.eventDao().getEventByTitle(date) as ArrayList<Event>
        Log.d("event", date + " 이벤트 호출")
        for( k in eventData)
            Log.d("event", "event : " + k.title)



        adapter.items = eventData
        CoroutineScope(Dispatchers.Main).launch {
            adapter.notifyDataSetChanged()
        }
    }

    fun delete(event: Event) {
        db.eventDao().deleteEvent(event)
        getEvent()
    }
}