package com.example.plantogether.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.plantogether.databinding.ActivityEventInfoBinding
import com.example.plantogether.dialog.InviteDialog

class EventInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityEventInfoBinding
    lateinit var date: String

    var fm = supportFragmentManager
    var inviteDialog = InviteDialog()
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
                var bundle = Bundle()
                bundle.putString("date", date)
                inviteDialog.arguments = bundle
                inviteDialog.show(fm, "dialog")
            }
        }
    }
}