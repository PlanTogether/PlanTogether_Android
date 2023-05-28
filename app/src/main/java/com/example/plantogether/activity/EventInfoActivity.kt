package com.example.plantogether.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.plantogether.databinding.ActivityEventInfoBinding
import com.example.plantogether.dialog.InviteDialog
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.share.model.SharingResult
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.template.model.TextTemplate

class EventInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityEventInfoBinding
    var date = ""
    var ok = 0

    companion object {
        const val TAG = "EventInfoActivity"
    }

    var defaultText = TextTemplate(
        text = """ ${date} 에 생성된 문자입니다.
        카카오톡 공유는 카카오톡을 실행하여
        사용자가 선택한 채팅방으로 메시지를 전송합니다.
    """.trimIndent(),
        link = Link(
            webUrl = "https://developers.kakao.com",
            mobileWebUrl = "https://developers.kakao.com"
        )
    )

    var fm = supportFragmentManager
    var inviteDialog = InviteDialog()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = getIntent()
        date = intent.getStringExtra("date").toString()

        initLayout()
        initBtn()
        binding = ActivityEventInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //buttoninit()
    }

//    fun buttoninit(){
//
//        binding.apply {
//            editButton.setOnClickListener {
//                //수정 화면으로 이동
//                val editintent = Intent(this@EventInfoActivity, EditEventActivity::class.java)
//                startActivity(editintent)
//            }
//        }
//
//    }
    private fun initLayout() {
        binding.apply {
            eventDate.text = date
        }
    }
    private fun initBtn() {
        binding.apply {
            sendInvitation.setOnClickListener {
                var bundle = Bundle()
                bundle.putString("date", date)
                inviteDialog.arguments = bundle
                inviteDialog.show(fm, "dialog")

            }
        }
    }

    public fun setOk() {
        date = binding.eventDate.text.toString()
        defaultText = TextTemplate(
            text = """ ${date} 에 생성된 문자입니다.
        카카오톡 공유는 카카오톡을 실행하여
        사용자가 선택한 채팅방으로 메시지를 전송합니다.
    """.trimIndent(),
            link = Link(
                webUrl = "https://developers.kakao.com",
                mobileWebUrl = "https://developers.kakao.com"
            )
        )
        sendMessage()
    }

    private val callback:  (SharingResult?, Throwable?) -> Unit = { sharingResult: SharingResult?, throwable: Throwable? ->
    }

    private fun sendMessage() {
        if (ShareClient.instance.isKakaoTalkSharingAvailable(this@EventInfoActivity)) {
            // 카카오톡으로 카카오톡 공유 가능
            ShareClient.instance.shareDefault(this@EventInfoActivity, defaultText) { sharingResult, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡 공유 실패", error)
                }
                else if (sharingResult != null) {
                    Log.d(TAG, "카카오톡 공유 성공 ${sharingResult.intent}")
                    startActivity(sharingResult.intent)


                    // 카카오톡 공유에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                    Log.w(TAG, "Warning Msg: ${sharingResult.warningMsg}")
                    Log.w(TAG, "Argument Msg: ${sharingResult.argumentMsg}")
                }
            }
        } else {
            // 카카오톡 미설치: 웹 공유 사용 권장
            // 웹 공유 예시 코드
            val sharerUrl = WebSharerClient.instance.makeDefaultUrl(defaultText)

            // CustomTabs으로 웹 브라우저 열기

            // 1. CustomTabsServiceConnection 지원 브라우저 열기
            // ex) Chrome, 삼성 인터넷, FireFox, 웨일 등
            try {
                KakaoCustomTabsClient.openWithDefault(this@EventInfoActivity, sharerUrl)
            } catch(e: UnsupportedOperationException) {
                // CustomTabsServiceConnection 지원 브라우저가 없을 때 예외처리
            }

            // 2. CustomTabsServiceConnection 미지원 브라우저 열기
            // ex) 다음, 네이버 등
            try {
                KakaoCustomTabsClient.open(this@EventInfoActivity, sharerUrl)
            } catch (e: ActivityNotFoundException) {
                // 디바이스에 설치된 인터넷 브라우저가 없을 때 예외처리
            }
        }
    }

}

