package com.example.plantogether.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.plantogether.R
import com.example.plantogether.activity.EventInfoActivity
import com.example.plantogether.activity.MainActivity
import com.example.plantogether.dialog.data.EventData
import com.example.plantogether.databinding.ActivityAddPlanDialogBinding
import com.example.plantogether.databinding.FragmentCalendarBinding
import com.example.plantogether.roomDB.EventDatabase
import com.example.plantogether.roomDB.Notice
import com.example.plantogether.roomDB.Plan
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kakao.sdk.common.KakaoSdk.init
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime


//일정 입력후 아래에서 나오는 다이얼로그
class AddPlanDialogActivity(private val date : String) : BottomSheetDialogFragment() {

    interface ItemClickListener {
        fun changeData()
    }

    private var itemClickListener :ItemClickListener?=null

    fun setItemClickListener(listener : ItemClickListener) {
        itemClickListener = listener
    }

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
            //일정 추가 버튼을 눌렀을 시:
            checkBtn.setOnClickListener {
                val activityContext = requireContext()
                var db: EventDatabase =  EventDatabase.getDatabase(activityContext)
                val plan = setPlan.text.toString()
                val time = setTime.text.toString()
                val newPlan = Plan(
                    id = 0,
                    title = plan,
                    date = date,
                    time = time
                )

                val initDate = LocalDate.now()
                val newNotice = Notice(
                    id = 0,
                    pid = newPlan.id,
                    title = newPlan.title,
                    date = initDate.toString(),
                    time = LocalTime.now().toString(),
                    type = 0
                )




                CoroutineScope(Dispatchers.IO).launch {
                    db.eventDao().insertPlan(newPlan)
                    db.eventDao().insertNotice(newNotice)
                }
                //insert plan 이후 dismiss
                dismiss()
                Toast.makeText(activityContext,"생성완료",Toast.LENGTH_SHORT).show()
                itemClickListener?.changeData()
            }
        }
    }
}