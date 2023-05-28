package com.example.plantogether.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.plantogether.R
import com.example.plantogether.databinding.ActivityEventInfoBinding

class EventInfoActivity : AppCompatActivity() {

    lateinit var binding: ActivityEventInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        buttoninit()
    }

    fun buttoninit(){

        binding.apply {
            editButton.setOnClickListener {
                //수정 화면으로 이동
                val editintent = Intent(this@EventInfoActivity, EditEventActivity::class.java)
                startActivity(editintent)
            }
            inviteButton.setOnClickListener {
                //초대창 띄우기
                val inviteintent = Intent(this@EventInfoActivity, InviteDialogActivity::class.java)
                startActivity(inviteintent)
            }
        }

    }
}