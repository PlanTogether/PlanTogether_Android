package com.example.plantogether.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.plantogether.databinding.ActivityMapBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback


class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding: ActivityMapBinding
    // lateinit var mapView: MapView
    // lateinit var naverMap: NaverMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // naver map
        //mapView = binding.mapView
        //mapView.onCreate(savedInstanceState)
        //mapView.getMapAsync(this)
    }

    override fun onMapReady(p0: NaverMap) {
        // naverMap = p0
        val cameraPosition = CameraPosition(
            LatLng( 37.5407624841263,
                127.07654675326717),  // 위치 지정
            9.0 // 줌 레벨
        )
        // naverMap.setCameraPosition(cameraPosition)
    }
}