package com.example.plantogether.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.plantogether.R
import com.example.plantogether.data.EventData
import com.example.plantogether.databinding.ActivityMakeEventBinding

class MakeEventActivity : AppCompatActivity() {
    lateinit var binding: ActivityMakeEventBinding
    val data:ArrayList<EventData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakeEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        buttoninit()
    }

    fun buttoninit(){
        binding.apply {
            addButton.setOnClickListener {
                //데이터셋 추가 로직,
                val title = binding.eventTitle.text.toString()
                val place = binding.eventPlace.text.toString()
                val date = binding.eventDate.text.toString()
                val detailInfo = binding.eventDetailInfo.text.toString()

                data.add(EventData(title, place, date, detailInfo))//이부분을 DB로
                clearEditText()

            }

            cancelButton.setOnClickListener {//수정 필요
                // 취소시 DateView로 돌아가야함
                finish()
            }

            selectfromMapButton.setOnClickListener {
                //MapView로 이동하면서 장소 마킹
                val mapintent = Intent(this@MakeEventActivity, MapActivity::class.java)
                startActivity(mapintent)
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