package com.example.plantogether.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.plantogether.databinding.ActivityEditEventBinding
import com.example.plantogether.R

class EditEventActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditEventBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        buttoninit()
    }
    fun buttoninit(){
        binding.apply {

            cancelButton.setOnClickListener {
                val cancelintent = Intent(this@EditEventActivity, EventInfoActivity::class.java)
                startActivity(cancelintent)
            }

            editButton.setOnClickListener {//안의 데이터를 정리하는 작업이 필요
                val editintent = Intent(this@EditEventActivity, EventInfoActivity::class.java)
                startActivity(editintent)
            }
        }

    }
}