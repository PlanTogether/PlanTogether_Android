package com.example.plantogether.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.plantogether.adapter.EventDataAdapter
import com.example.plantogether.dialog.data.EventData
import com.example.plantogether.databinding.ActivityMakeEventBinding
import com.example.plantogether.roomDB.Event
import com.example.plantogether.roomDB.EventDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class MakeEventActivity : AppCompatActivity() {
    lateinit var binding: ActivityMakeEventBinding
    lateinit var  db: EventDatabase

    val data:ArrayList<EventData> = ArrayList()
    var date = ""

    lateinit var adapter: EventDataAdapter
    lateinit var rdb: DatabaseReference
    var userName: String = ""


    private val REQUEST_MAP_LOCATION = 1001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakeEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = getIntent()
        userName = intent.getStringExtra("userName").toString()
        date = intent.getStringExtra("date").toString()
        println("사용자명 : " + userName + " in MakeEventActivity")

        init()
    }

    fun init(){
        db = EventDatabase.getDatabase(this)
        rdb = Firebase.database.getReference("$userName/Events")
        binding.apply {

            eventDate.setText(date)

            // 추가 버튼을 눌렀을 때
            addButton.setOnClickListener {

                //데이터셋 추가 로직,
                val title = eventTitle.text.toString()
                val place = eventPlace.text.toString()
                val detail = eventDetailInfo.text.toString()

                if (title == "") {
                    Toast.makeText(this@MakeEventActivity,"제목은 입력해야 합니다.",
                        Toast.LENGTH_SHORT).show()
                }
                else {
                    val event = Event(0,1, title, place, date, "", detail)
                    CoroutineScope(Dispatchers.IO).launch {
                        // 이벤트를 저장할 때의 키값은 title(이벤트명)로 했습니다.
                        rdb.child(title).setValue(event)
                        db.eventDao().insertEvent(event)
                    }
                    clearEditText()
                    val intent = Intent(this@MakeEventActivity,
                        MainActivity::class.java)
                    startActivity(intent)
                }

            }
            // 취소 버튼을 눌렀을 때
            cancelButton.setOnClickListener {//수정 필요
                // 취소 시 DateView로 돌아가야함
                finish()
            }

            // 지도에서 선택 버튼을 눌렀을 때
            selectfromMapButton.setOnClickListener {
                //MapView로 이동하면서 장소 마킹
                val mapIntent = Intent(this@MakeEventActivity,
                    MapActivity::class.java)
                startActivityForResult(mapIntent, REQUEST_MAP_LOCATION)
            }
        }

    }

    fun clearEditText(){
        binding.apply{
            eventTitle.text.clear()
            eventPlace.text.clear()
            eventDetailInfo.text.clear()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("onActivityResult 호출")
        if (requestCode == REQUEST_MAP_LOCATION && resultCode == Activity.RESULT_OK) {
            // MapActivity에서 전달된 결과 데이터를 받아 처리
            val latitude = data?.getDoubleExtra("latitude", 0.0)
            val longitude = data?.getDoubleExtra("longitude", 0.0)
            val address = data?.getStringExtra("address")
            binding.eventPlace.setText(address)
        }
    }
}