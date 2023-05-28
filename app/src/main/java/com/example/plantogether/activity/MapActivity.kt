package com.example.plantogether.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.plantogether.R
import com.example.plantogether.databinding.ActivityMapBinding

class MapActivity : AppCompatActivity() {
    lateinit var binding: ActivityMapBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}