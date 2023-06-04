package com.example.plantogether.dialog

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantogether.activity.MakeEventActivity
import com.example.plantogether.adapter.DateViewAdapter
import com.example.plantogether.databinding.ActivityAddScheduleDialogBinding
import com.example.plantogether.dialog.data.EventData

class AddScheduleDialogActivity(private val context : AppCompatActivity) {

    lateinit var binding : ActivityAddScheduleDialogBinding
    lateinit var adapter : DateViewAdapter
    var planData : ArrayList<EventData> = ArrayList()

    val dlg = Dialog(context)

    fun show(todayDate : String) {
        binding = ActivityAddScheduleDialogBinding.inflate(context.layoutInflater)
        dlg.setContentView(binding.root)
        //바깥에 누르면 없어지게 만드는 기능
        dlg.setCancelable(true)
        initRecycle()


        binding.initDate.setText(todayDate)

        //일정추가
        binding.plusPlan.setOnClickListener {
            val bottomSheetDialogFragment = AddPlanDialogActivity()
            bottomSheetDialogFragment.show(context.supportFragmentManager, "bottom_sheet_dialog")
        }
        //이벤트추가
        binding.plusEvent.setOnClickListener {
            val intent = Intent(context, MakeEventActivity::class.java)
            context.startActivity(intent)

        }

        val window = dlg.window
        val layoutParams = window?.attributes
        layoutParams?.width = (context.resources.displayMetrics.widthPixels * 0.7).toInt()
        layoutParams?.height = (context.resources.displayMetrics.heightPixels * 0.7).toInt()
        window?.attributes = layoutParams
        dlg.show()
    }
    fun initRecycle() {
        planData.add(EventData("abc", "ass", "ddd", "s", 1))
        planData.add(EventData("abc2", "ass3", "ddd", "s", 1))
        planData.add(EventData("abc3", "ass2", "ddd", "s", 2))
        adapter = DateViewAdapter(planData)
        binding.eventRecycleView.adapter = adapter
        binding.eventRecycleView.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL, false)

        adapter.setOnItemClickListener(object : DateViewAdapter.OnItemClickListener {
            override fun OnItemClick(position: Int) {
                //이곳에 이벤트 클릭했을 때 나오는거 쓰면됨.
            }

            override fun OnDeleteItemClick(position: Int) {
                //삭제 버튼 눌렀을 때
                planData.removeAt(position)
                adapter.notifyDataSetChanged()
            }

        })
    }
}