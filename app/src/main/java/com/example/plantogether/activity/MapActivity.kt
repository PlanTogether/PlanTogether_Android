package com.example.plantogether.activity
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.plantogether.R
import com.example.plantogether.databinding.ActivityMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding: ActivityMapBinding
    lateinit var mapView: MapView
    lateinit var naverMap: NaverMap

    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationSource: FusedLocationSource
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        // naver map
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    fun init() {
        binding.apply {
            btnPlaceConfirm.setOnClickListener {
                val latitude = naverMap.cameraPosition.target.latitude
                val longitude = naverMap.cameraPosition.target.longitude
                val address = getAddress(latitude, longitude)
                println(address)
                val intent = Intent()
                intent.putExtra("latitude", latitude)
                intent.putExtra("longitude", longitude)
                intent.putExtra("address", address)

                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onMapReady(navermap: NaverMap) {
        naverMap = navermap
        val cameraPosition = CameraPosition(
            LatLng(
                37.5407624841263, 127.07654675326717
            ),  // 위치 지정
            15.0 // 줌 레벨
        )
        naverMap.setCameraPosition(cameraPosition)

        val marker = Marker()
        marker.position = LatLng(
            naverMap.cameraPosition.target.latitude, naverMap.cameraPosition.target.longitude
        )
        marker.icon = OverlayImage.fromResource(R.drawable.icon_marker_black)
        marker.map = naverMap



        naverMap.addOnCameraChangeListener { reason, animated ->
            Log.i("NaverMap", "카메라 변경 - reson: $reason, animated: $animated")
            marker.position = LatLng(
                // 현재 보이는 네이버맵의 정중앙 가운데로 마커 이동
                naverMap.cameraPosition.target.latitude, naverMap.cameraPosition.target.longitude
            )
        }

        naverMap.addOnCameraIdleListener {
            marker.position = LatLng(
                naverMap.cameraPosition.target.latitude, naverMap.cameraPosition.target.longitude
            )
        }


        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        var currentLocation: Location?
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            currentLocation = location
            // 위치 오버레이의 가시성은 기본적으로 false로 지정되어 있습니다.
            // 가시성을 true로 변경하면 지도에 위치 오버레이가 나타납니다.
            // 파랑색 점, 현재 위치 표시
            naverMap.locationOverlay.run {
                isVisible = true
                position = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
            }

            // 카메라 현재위치로 이동
            val cameraUpdate = CameraUpdate.scrollTo(
                LatLng(
                    currentLocation!!.latitude, currentLocation!!.longitude
                )
            )
            naverMap.moveCamera(cameraUpdate)

            // 마커 현재위치로 변경
            marker.position = LatLng(
                naverMap.cameraPosition.target.latitude,
                naverMap.cameraPosition.target.longitude
            )
        }

    }

    // 좌표 -> 주소 변환
    private fun getAddress(lat: Double, lng: Double): String {
        val geoCoder = Geocoder(this, Locale.KOREA)
        val address: ArrayList<Address>
        var addressResult = "주소를 가져 올 수 없습니다."
        try {
            //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
            //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
            address = geoCoder.getFromLocation(lat, lng, 1) as ArrayList<Address>
            if (address.size > 0) {
                // 주소 받아오기
                val currentLocationAddress = address[0].getAddressLine(0)
                    .toString()
                addressResult = currentLocationAddress
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressResult
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}