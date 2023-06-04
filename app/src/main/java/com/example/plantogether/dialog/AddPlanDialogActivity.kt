package com.example.plantogether.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.example.plantogether.R
import com.example.plantogether.dialog.data.EventData
import com.example.plantogether.databinding.ActivityAddPlanDialogBinding
import com.example.plantogether.databinding.FragmentCalendarBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


//일정 입력후 아래에서 나오는 다이얼로그
class AddPlanDialogActivity : BottomSheetDialogFragment() {

    lateinit var binding : ActivityAddPlanDialogBinding

    interface BottomSheetListener {
        fun onXButtonClicked()
    }
    private var bottomSheetListener : BottomSheetListener ?= null
    val data:ArrayList<EventData> = ArrayList()
    val selected:ArrayList<Boolean> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityAddPlanDialogBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun init() {
        binding.apply {
            backBtn.setOnClickListener {
                dismiss()
            }
            checkBtn.setOnClickListener {
                val date = setDate.text.toString()
                val time = setTime.text.toString()
                val intent = Intent(requireContext(), AddScheduleDialogActivity::class.java)
                intent.putExtra("inputDate", date)
                intent.putExtra("inputTime", time)
                dismiss()
            }
        }
    }
}