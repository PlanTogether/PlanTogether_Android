package com.example.plantogether.dialog


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantogether.adapter.EventDataAdapter
import com.example.plantogether.dialog.data.EventData
import com.example.plantogether.databinding.ActivityAddScheduleDialogUnusedBinding
import com.example.plantogether.roomDB.Event


class AddScheduleDialogUnusedActivity : AppCompatActivity() {

    lateinit var binding : ActivityAddScheduleDialogUnusedBinding
    val data:ArrayList<Event> = ArrayList()
    val selected:ArrayList<Boolean> = ArrayList()
    lateinit var adapter: EventDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddScheduleDialogUnusedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecycle()

        //일정 생성한경우
        if(intent.getBooleanExtra("updateAdapter", false)) {
            adapter.notifyDataSetChanged()
        }

        binding.addPlan.setOnClickListener {
            val bottomSheetDialogFragment = AddPlanDialogActivity("abc")
            bottomSheetDialogFragment.show(supportFragmentManager, "bottom_sheet_dialog")
        }
    }

    private fun initRecycle() {
        binding.recyclerViewEventCalendar.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)

        for(i in 0..10) {
            //data.add(Event("a","b","2023-05-30","여기", 1))
            selected.add(false)
        }
        adapter = EventDataAdapter(data, selected)
        binding.recyclerViewEventCalendar.adapter = adapter

        val inputText = intent.getStringExtra("inputDate")
        val inputText2 = intent.getStringExtra("inputTime")

        if(inputText != null && inputText2 != null) {
            if(inputText != "" && inputText2 != "") {
                //data.add(EventData(inputText, "", inputText2, "", 1))
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "추가 완료", Toast.LENGTH_SHORT).show()
            }
        }


    }



}