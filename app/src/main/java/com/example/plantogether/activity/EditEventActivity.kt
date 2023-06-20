package com.example.plantogether.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.plantogether.databinding.ActivityEditEventBinding
import com.example.plantogether.data.EventData
import com.example.plantogether.data.NoticeData
import com.example.plantogether.roomDB.EventDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

@Suppress("DEPRECATION")
class EditEventActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditEventBinding

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
                    val setEventDataTask = async { rdb.child(id).setValue(newEventData).await() }

                    for (invitee in event.participantName) {
                        if (invitee != userName) {
                            val ref = Firebase.database.getReference("$invitee/Events")
                            val ref2 = Firebase.database.getReference("$invitee/Notices")
                            async { ref.child(id).setValue(newEventData).await() }
                            val newNoticeRef = ref2.push()
                            val newNoticeRefKey = newNoticeRef.key
                            val now = System.currentTimeMillis()
                            val text = "이벤트가 수정되었습니다."
                            val noticeData = NoticeData(newNoticeRefKey.toString(),
                                newEventData.title.toString(), now, text)
                            newNoticeRef.setValue(noticeData)
                        }
                    }

                    setEventDataTask.await() // 메인 이벤트 데이터 설정이 완료될 때까지 기다립니다.

                    withContext(Dispatchers.Main) {
                        val editintent = Intent()
                        editintent.putExtra("dataChanged", true)
                        editintent.putExtra("id",id)
                        editintent.putExtra("userName",userName)
                        setResult(Activity.RESULT_OK, editintent)
                        finish()
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