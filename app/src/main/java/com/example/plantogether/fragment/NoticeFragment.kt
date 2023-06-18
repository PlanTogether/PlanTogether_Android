package com.example.plantogether.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.plantogether.R
import com.google.firebase.database.DatabaseReference

class NoticeFragment : Fragment() {

    lateinit var rdb: DatabaseReference
    var userName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userName = arguments?.getString("userName").toString()
        // println("사용자명 : " + userName + " in NoticeFragment")
    }
}