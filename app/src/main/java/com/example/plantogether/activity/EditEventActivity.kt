package com.example.plantogether.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.plantogether.databinding.ActivityEditEventBinding
import com.example.plantogether.R
import com.example.plantogether.dialog.data.EventData

@Suppress("DEPRECATION")
class EditEventActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditEventBinding
    val data:ArrayList<EventData> = ArrayList()

    private val REQUEST_MAP_LOCATION = 1001
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

                data.add(EventData(title, place, date, detailInfo, 1))
                // 이부분은 DB로 그리고 받아온 event의 id로 add->replace
                clearEditText()

                val editintent = Intent(this@EditEventActivity, EventInfoActivity::class.java)
                startActivity(editintent)
            }
            selectfromMapButton.setOnClickListener {
                //MapView로 이동하면서 장소 마킹
                val mapintent = Intent(this@EditEventActivity,
                    MapActivity::class.java)
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