package com.example.plantogether.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.plantogether.R
import com.example.plantogether.databinding.FragmentInviteDialogBinding

class InviteDialog : DialogFragment() {

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
        binding = FragmentInviteDialogBinding.inflate(inflater,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBtn()
    }

    private fun initBtn() {
        binding?.apply {

            inviteText.text = "나 이거 초대 보낼랭"

            okBtn.setOnClickListener {
                Toast.makeText(activity,inviteText.text.toString(),Toast.LENGTH_SHORT).show()
            }
        }
    }

}