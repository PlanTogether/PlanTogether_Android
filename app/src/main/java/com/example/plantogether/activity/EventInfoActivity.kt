package com.example.plantogether.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.plantogether.R
import com.example.plantogether.databinding.ActivityEventInfoBinding
import com.example.plantogether.databinding.ActivityMainBinding

class EventInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityEventInfoBinding
    lateinit var date: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = getIntent()
        date = intent.getStringExtra("date").toString()
        initLayout()
        initBtn()
    }
    private fun initLayout() {
        binding.apply {
            eventDate.text = date
        }
    }
    private fun initBtn() {
        binding.apply {
            sendInvitation.setOnClickListener {
                //dialogue 호출
            }
        }
    }
}