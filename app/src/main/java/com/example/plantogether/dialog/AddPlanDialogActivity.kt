package com.example.plantogether.dialog

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
                val planData = EventData(0,2, title, "", date, time, "")
                val newPlanRef = rdb.push()
                val newPlanKey = newPlanRef.key
                newPlanRef.setValue(planData)
                // 일정을 저장할 때의 키값은 title(일정명)로 했습니다.
                rdb.child(title).setValue(planData)
                clearEditText()
                dismiss()
            }
        }
    }

    fun clearEditText(){
        binding.apply{
            setTitle.text.clear()
            setTime.text.clear()
        }
    }

    fun setUserNameInPlan(userName: String) {
        this.userName = userName
    }

    fun setDateInPlan(date: String) {
        this.date = date
    }
}