package com.example.plantogether.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.plantogether.databinding.ActivityEditEventBinding
import com.example.plantogether.R
import com.example.plantogether.dialog.data.EventData

class EditEventActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditEventBinding
    val data:ArrayList<EventData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        buttoninit()
    }

    var pos = -1
    private fun initData() {
        binding.eventTitle.setText(data[pos].title)
        binding.eventPlace.setText(data[pos].place)
        binding.eventDate.setText(data[pos].date)
        binding.eventDetailInfo.setText(data[pos].detailInfo)
    }

    fun buttoninit(){
        binding.apply {

            cancelButton.setOnClickListener {
                val cancelintent = Intent(this@EditEventActivity, EventInfoActivity::class.java)
                startActivity(cancelintent)
            }

            editButton.setOnClickListener {//안의 데이터를 정리하는 작업이 필요

                val title = binding.eventTitle.text.toString()
                val place = binding.eventPlace.text.toString()
                val date = binding.eventDate.text.toString()
                val detailInfo = binding.eventDetailInfo.text.toString()

                data.add(EventData(title, place, date, detailInfo))//이부분은 DB로 그리고 받아온 event의 id로 add->replace
                clearEditText()

                val editintent = Intent(this@EditEventActivity, EventInfoActivity::class.java)
                startActivity(editintent)
            }
        }

    }

    fun clearEditText(){
        binding.apply{
            eventTitle.text.clear()
            eventPlace.text.clear()
            eventDate.text.clear()
            eventDetailInfo.text.clear()
        }
    }
}