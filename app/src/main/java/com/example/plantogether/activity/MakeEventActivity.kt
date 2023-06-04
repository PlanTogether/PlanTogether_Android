package com.example.plantogether.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.plantogether.R
import com.example.plantogether.dialog.data.EventData
import com.example.plantogether.databinding.ActivityMakeEventBinding
import com.example.plantogether.roomDB.Event
import com.example.plantogether.roomDB.EventDatabase
import com.kakao.sdk.common.KakaoSdk.init
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MakeEventActivity : AppCompatActivity() {
    lateinit var binding: ActivityMakeEventBinding
    lateinit var  db: EventDatabase

    val data:ArrayList<EventData> = ArrayList()
    var date = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakeEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = getIntent()
        date = intent.getStringExtra("date").toString()

        init()
    }

    fun init(){
        db = EventDatabase.getDatabase(this)
        binding.apply {

            eventDate.setText(date)
            addButton.setOnClickListener {

                //데이터셋 추가 로직,
                val title = eventTitle.text.toString()
                val place = eventPlace.text.toString()
                val detail = eventDetailInfo.text.toString()

                if (title == "") {
                    Toast.makeText(this@MakeEventActivity,"제목은 입력해야 합니다.", Toast.LENGTH_SHORT).show()
                }
                else {
                    val event = Event(0,1, title, place, date, "", detail)
                    CoroutineScope(Dispatchers.IO).launch {
                        db.eventDao().insertEvent(event)
                    }
                    clearEditText()
                    val intent = Intent(this@MakeEventActivity,
                        MainActivity::class.java)
                    startActivity(intent)
                }

            }

            cancelButton.setOnClickListener {//수정 필요
                // 취소시 DateView로 돌아가야함
                finish()
            }

            selectfromMapButton.setOnClickListener {
                //MapView로 이동하면서 장소 마킹
                val mapintent = Intent(this@MakeEventActivity,
                    MapActivity::class.java)
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