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
        val view = inflater.inflate(R.layout.activity_add_plan_dialog, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button : ImageView = view.findViewById(R.id.XBtn)
        val button2 : ImageView = view.findViewById(R.id.VBtn)
        button.setOnClickListener {
            dismiss()
        }
        button2.setOnClickListener {
            val inputDate : EditText = view.findViewById(R.id.setDate)
            val inputTime : EditText = view.findViewById(R.id.setTime)

            val intent = Intent(requireContext(), AddScheduleDialogUnusedActivity::class.java)
            intent.putExtra("inputDate", inputDate.text)
            intent.putExtra("inputTime", inputTime.text)
            dismiss()

        }

    }



}