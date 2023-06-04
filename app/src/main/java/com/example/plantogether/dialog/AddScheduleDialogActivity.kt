package com.example.plantogether.dialog

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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
        initLayout()

        val window = dlg.window
        val layoutParams = window?.attributes
        layoutParams?.width = (context.resources.displayMetrics.widthPixels * 0.7).toInt()
        layoutParams?.height = (context.resources.displayMetrics.heightPixels * 0.7).toInt()
        window?.attributes = layoutParams
        dlg.show()
    }
    fun initRecyclerView() {

        binding.eventRecycleView.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL, false)

        adapter.setOnItemClickListener(object : DateViewAdapter.OnItemClickListener {
            override fun OnItemClick(position: Int) {
                //이곳에 이벤트 클릭했을 때 나오는거 쓰면됨.
            }

            override fun OnDeleteItemClick(position: Int) {
                //삭제 버튼 눌렀을 때
                eventData.removeAt(position)
                adapter.notifyDataSetChanged()
            }

        })

        binding.eventRecycleView.adapter= adapter
    }

    fun initLayout() {
        db = EventDatabase.getDatabase(context)
        CoroutineScope(Dispatchers.IO).launch {
            getEvent()
        }
        binding.initDate.setText(date)

        //일정추가
        binding.plusPlan.setOnClickListener {
            val bottomSheetDialogFragment = AddPlanDialogActivity()
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
        adapter.items = eventData
        CoroutineScope(Dispatchers.Main).launch {
            adapter.notifyDataSetChanged()
        }
    }
}