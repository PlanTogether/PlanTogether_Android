package com.example.plantogether.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import android.widget.Toast
import com.example.plantogether.databinding.ActivityEditEventBinding
import com.example.plantogether.R
import com.example.plantogether.dialog.data.EventData
import com.example.plantogether.roomDB.Event
import com.example.plantogether.roomDB.EventDatabase
import com.example.plantogether.roomDB.Notice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import java.util.ArrayList

@Suppress("DEPRECATION")
class EditEventActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditEventBinding

    lateinit var db: EventDatabase

    var id = -1
    lateinit var event: Event

    /* addscheduledialogactivity 업데이트용
    interface OnItemClickListener {
        fun onItemClick()
    }
    var itemClickListener : OnItemClickListener ? = null
    fun setOnClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }*/

    private val REQUEST_MAP_LOCATION = 1001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = getIntent()
        id = intent.getIntExtra("id", -1)
        db = EventDatabase.getDatabase(this)
        CoroutineScope(Dispatchers.IO).launch {
            event = db.eventDao().getEventById(id)
            withContext(Dispatchers.Main) {
                initLayout()
            }
        }
    }

    var pos = -1
    private fun initLayout() {
        binding.apply {
            eventTitle.setText(event.title)
            eventPlace.setText(event.place)
            eventDate.setText(event.date)
            eventDetailInfo.setText(event.detail)

            cancelButton.setOnClickListener {
                finish()
            }

            selectfromMapButton.setOnClickListener {
                //MapView로 이동하면서 장소 마킹
                val mapintent = Intent(
                    this@EditEventActivity,
                    MapActivity::class.java
                )
                startActivityForResult(mapintent, REQUEST_MAP_LOCATION)
            }

            editButton.setOnClickListener {//안의 데이터를 정리하는 작업이 필요
                val title = binding.eventTitle.text.toString()
                val place = binding.eventPlace.text.toString()
                val detail = binding.eventDetailInfo.text.toString()

                val newEvent = Event(event.id, 1, title, place, event.date, "", detail)
                val newNotice = Notice(0, newEvent.id, newEvent.title, LocalDate.now().toString(), LocalTime.now().toString(), 5)
                CoroutineScope(Dispatchers.IO).launch {
                    val editedID = db.eventDao().updateEvent(newEvent)
                    newNotice.pid = editedID
                    db.eventDao().insertNotice(newNotice)
                }
                Toast.makeText(this@EditEventActivity, "수정 완료", Toast.LENGTH_SHORT).show()
                // 이부분은 DB로 그리고 받아온 event의 id로 add->replace
                clearEditText()
                finish()
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
        if (requestCode == REQUEST_MAP_LOCATION && resultCode == Activity.RESULT_OK) {
            // MapActivity에서 전달된 결과 데이터를 받아 처리
            val latitude = data?.getDoubleExtra("latitude", 0.0)
            val longitude = data?.getDoubleExtra("longitude", 0.0)
            val address = data?.getStringExtra("address")
            binding.eventPlace.setText(address)
        }
    }
}