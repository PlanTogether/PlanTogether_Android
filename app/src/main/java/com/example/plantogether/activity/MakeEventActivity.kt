package com.example.plantogether.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.plantogether.R
import com.example.plantogether.data.EventData
import com.example.plantogether.databinding.ActivityMakeEventBinding
import com.example.plantogether.roomDB.Event
import com.example.plantogether.roomDB.EventDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MakeEventActivity : AppCompatActivity() {
    lateinit var binding: ActivityMakeEventBinding
    lateinit var  db: EventDatabase

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
                val detail = binding.eventDetailInfo.text.toString()

                if (title == "") {
                    Toast.makeText(this@MakeEventActivity,"제목은 입력해야 합니다.", Toast.LENGTH_SHORT).show()
                }
                else {
                    val event = Event(0, title, place, date, detail)
                    CoroutineScope(Dispatchers.IO).launch {
                        db.eventDao().insertEvent(event)
                    }
                    clearEditText()
                }

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