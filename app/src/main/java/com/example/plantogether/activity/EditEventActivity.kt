package com.example.plantogether.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import com.example.plantogether.databinding.ActivityEditEventBinding
import com.example.plantogether.R
import com.example.plantogether.dialog.data.EventData
import com.example.plantogether.roomDB.Event
import com.example.plantogether.roomDB.EventDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditEventActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditEventBinding

    lateinit var db : EventDatabase

    var id = -1
    lateinit var event: Event
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = getIntent()
        id = intent.getIntExtra("id",-1)
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
                        intent.putExtra("id",event.id)
                        startActivity(editintent)
                    }
                }

                // 이부분은 DB로 그리고 받아온 event의 id로 add->replace
                clearEditText()
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