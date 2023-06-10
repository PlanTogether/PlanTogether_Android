package com.example.plantogether.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.plantogether.databinding.ActivityEditEventBinding
import com.example.plantogether.roomDB.Event
import com.example.plantogether.roomDB.EventDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("DEPRECATION")
class EditEventActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditEventBinding

    lateinit var db: EventDatabase

    var id = -1
    lateinit var event: Event

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

            editButton.setOnClickListener {//안의 데이터를 정리하는 작업이 필요
                val title = binding.eventTitle.text.toString()
                val place = binding.eventPlace.text.toString()
                val detail = binding.eventDetailInfo.text.toString()

                val newEvent = Event(event.id, 1, title, place, event.date, "", detail)
                CoroutineScope(Dispatchers.IO).launch {
                    db.eventDao().updateEvent(newEvent)
                    withContext(Dispatchers.Main) {
                        val editintent = Intent(this@EditEventActivity, EventInfoActivity::class.java)
                        intent.putExtra("id", event.id)
                        startActivity(editintent)
                    }
                }


            }
            selectfromMapButton.setOnClickListener {
                //MapView로 이동하면서 장소 마킹
                val mapintent = Intent(
                    this@EditEventActivity,
                    MapActivity::class.java
                )
                startActivityForResult(mapintent, REQUEST_MAP_LOCATION)
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
