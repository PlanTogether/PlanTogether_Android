package com.example.plantogether.dialog

import android.R
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.plantogether.data.EventData
import com.example.plantogether.databinding.ActivityAddPlanDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


//일정 입력후 아래에서 나오는 다이얼로그
class AddPlanDialogActivity : BottomSheetDialogFragment() {

    lateinit var binding : ActivityAddPlanDialogBinding

    lateinit var rdb: DatabaseReference
    private var userName: String = ""
    var date: String = ""
    interface BottomSheetListener {
        fun onXButtonClicked()
    }


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
        println("사용자명 : " + userName + " in AddPlanDialogActivity")
        rdb = Firebase.database.getReference("$userName/Events")

        binding.apply {
            backBtn.setOnClickListener {
                dismiss()
            }
            checkBtn.setOnClickListener {
                val title = setTitle.text.toString()
                val time = setTime.text.toString()
                val newPlanRef = rdb.push()
                val newPlanKey = newPlanRef.key
                val planData = EventData(newPlanKey.toString(),
                    2, title, "", date, time, "")
                newPlanRef.setValue(planData)
                clearEditText()
                dismiss()
            }
            setTime.setOnClickListener {
                val listener =
                    OnTimeSetListener { view, hourOfDay, minute ->
                        var timeString = hourOfDay.toString() + "시 " + minute.toString() + "분"
                        binding.setTime.text = timeString
                    }
                val dialog = TimePickerDialog(
                    context,
                    R.style.Theme_Holo_Light_Dialog,
                    listener,
                    15,
                    30,
                    false
                )
                dialog.setTitle("시간을 선택해주세요")
                dialog.show()
            }
        }
    }

    fun clearEditText(){
        binding.apply{
            setTitle.text.clear()
        }
    }

    fun setUserNameInPlan(userName: String) {
        this.userName = userName
    }

    fun setDateInPlan(date: String) {
        this.date = date
    }
}