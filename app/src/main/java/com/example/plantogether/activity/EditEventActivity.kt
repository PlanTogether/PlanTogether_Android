package com.example.plantogether.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.plantogether.databinding.ActivityEditEventBinding
import com.example.plantogether.data.EventData
import com.example.plantogether.roomDB.Event
import com.example.plantogether.roomDB.EventDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("DEPRECATION")
class EditEventActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditEventBinding

    lateinit var db: EventDatabase

    private val REQUEST_MAP_LOCATION = 1001

    lateinit var event: EventData
    lateinit var rdb: DatabaseReference
    var userName: String = ""
    var id: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = getIntent()
        userName = intent.getStringExtra("userName").toString()
        id = intent.getStringExtra("id").toString()
        event = (intent.getParcelableExtra("event") as? EventData)!!
        // println("사용자명 : " + userName + " in EditEventActivity")
        // db = EventDatabase.getDatabase(this)
        rdb = Firebase.database.getReference("$userName/Events")
        CoroutineScope(Dispatchers.IO).launch {
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
                val newEventData =
                    EventData(id,
                        1, title, place, event.date, "", detail, event.participantName)

                CoroutineScope(Dispatchers.IO).launch {
                    rdb.child(id).setValue(newEventData)
                    for (invitee in event.participantName) {
                        if (invitee != userName)
                        {
                            var ref = Firebase.database.getReference("$invitee/Events")
                            ref.child(id).setValue(newEventData)
                        }
                    }
                    withContext(Dispatchers.Main) {
                        val editintent = Intent(this@EditEventActivity, EventInfoActivity::class.java)
                        intent.putExtra("id", event.id)
                        Log.d("id", event.id)
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