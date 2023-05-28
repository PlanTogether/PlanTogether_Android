package com.example.plantogether.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.plantogether.R
import com.example.plantogether.databinding.FragmentInviteDialogBinding

class InviteDialog : DialogFragment() {

    var date = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    private var binding: FragmentInviteDialogBinding?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var bundle = arguments
        date = bundle!!.getString("date",date)

        binding = FragmentInviteDialogBinding.inflate(inflater,container,false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return binding!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBtn()
    }

    private fun initBtn() {
        binding?.apply {

            inviteText.text = "$date 나 이거 초대 보낼랭"

            okBtn.setOnClickListener {
                Toast.makeText(activity,inviteText.text.toString(),Toast.LENGTH_SHORT).show()
            }
        }
    }

}