package com.example.plantogether.dialog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plantogether.R
import com.example.plantogether.adapter.EventDataAdapter
import com.example.plantogether.data.EventData
import com.example.plantogether.databinding.ActivityAddScheduleDialog2Binding
import com.example.plantogether.databinding.ActivityAddScheduleDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.internal.notify

class AddScheduleDialogActivity : AppCompatActivity() {

    lateinit var binding : ActivityAddScheduleDialogBinding
    val data:ArrayList<EventData> = ArrayList()
    val selected:ArrayList<Boolean> = ArrayList()
    lateinit var adapter: EventDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddScheduleDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecycle()

        binding.addPlan.setOnClickListener {
            val bottomSheetDialogFragment = AddScheduleDialog2Activity()
            bottomSheetDialogFragment.show(supportFragmentManager, "bottom_sheet_dialog")
        }
    }

    private fun initRecycle() {
        binding.recyclerViewEventCalendar.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)

        for(i in 0..10) {
            data.add(EventData("a","b","2023-05-30","여기"))
            selected.add(false)
        }
        adapter = EventDataAdapter(data, selected)
        binding.recyclerViewEventCalendar.adapter = adapter

        val inputText = intent.getStringExtra("inputDate")
        val inputText2 = intent.getStringExtra("inputTime")

        if(inputText != null && inputText2 != null) {
            if(inputText != "" && inputText2 != "") {
                data.add(EventData(inputText, "", inputText2, ""))
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "추가 완료", Toast.LENGTH_SHORT).show()
            }
        }


    }



}