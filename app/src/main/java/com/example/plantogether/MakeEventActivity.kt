package com.example.plantogether

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.plantogether.databinding.ActivityMakeEventBinding

class MakeEventActivity : AppCompatActivity() {
    lateinit var binding: ActivityMakeEventBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakeEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        buttoninit()
    }

    fun buttoninit(){
        binding.apply {
            addButton.setOnClickListener {
                //데이터셋 추가 로직
            }

            cancelButton.setOnClickListener {//수정 필요
                // 취소시 DateView로 돌아가야함
                val cancelintent = Intent(this@MakeEventActivity, EventInfoActivity::class.java)
                startActivity(cancelintent)
            }

            selectfromMapButton.setOnClickListener {
                //MapView로 이동하면서 장소 마킹
                val mapintent = Intent(this@MakeEventActivity, MapActivity::class.java)
                startActivity(mapintent)
            }
        }

    }


}