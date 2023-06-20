package com.example.plantogether.activity

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.plantogether.data.EventData
import com.example.plantogether.databinding.ActivityEventInfoBinding
import com.example.plantogether.dialog.InviteDialog
import com.example.plantogether.roomDB.EventDatabase
import com.example.plantogether.roomDB.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.dynamiclinks.DynamicLink.AndroidParameters
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.share.model.SharingResult
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.template.model.TextTemplate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EventInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityEventInfoBinding

    companion object {
        const val TAG = "EventInfoActivity"
        const val EDIT_EVENT_REQUEST_CODE = 1
    }

    lateinit var defaultText: TextTemplate
    lateinit var text: String

    var fm = supportFragmentManager
    var inviteDialog = InviteDialog()

    lateinit var event: EventData
    lateinit var rdb: DatabaseReference
    var userName: String = ""
    var id: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = getIntent()
        userName = intent.getStringExtra("userName").toString()
        id = intent.getStringExtra("id").toString()
        // println("현재 이벤트정보의 키 값(ID) : " + id)
        // println("사용자명 : " + userName + " in EventInfoActivity")

        getEventInfo()
    }

    private fun initLayout() {
        binding.apply {
            eventTitle.setText(event.title)
            eventPlace.setText(event.place)
            eventDate.setText(event.date)
            eventDetailInfo.setText(event.detail)
        }
    }
    private fun initBtn() {
        binding.apply {
            sendInvitation.setOnClickListener {
                var bundle = Bundle()
                bundle.putString("text", text)
                inviteDialog.arguments = bundle
                inviteDialog.show(fm, "dialog")

            }
            editButton.setOnClickListener {
                // 현재 사용자가 해당 이벤트를 최초로 만든 사람일 때 -> 수정 가능
                if (event.participantName[0] == userName) {
                    // 수정 화면으로 이동
                    val editintent = Intent(this@EventInfoActivity, EditEventActivity::class.java)
                    editintent.putExtra("id",event.id)
                    editintent.putExtra("userName",userName)
                    editintent.putExtra("event", event as Parcelable)
                    startActivityForResult(editintent, EDIT_EVENT_REQUEST_CODE)
                }
                // 현재 사용자가 해당 이벤트에 대해서 초대장을 받아 참가한 참가자일 때 -> 수정 불가
                else {
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(this@EventInfoActivity,
                            "해당 이벤트의 최초 생성자만 수정이 가능합니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            deleteButton.setOnClickListener {

            }
        }
    }

    public fun setOk() {
        getLink()
    }

    private val callback:  (SharingResult?, Throwable?) -> Unit = { sharingResult: SharingResult?, throwable: Throwable? ->
    }

    private fun sendMessage(url: String) {
        val link = Uri.parse(url)
        Log.d("dynamic3", link.toString())
        defaultText =  TextTemplate(
            text = """ ${userName}님이 [${event.title}] 이벤트에 초대했습니다.
                      
일시 : ${event.date}
                        
장소 : ${event.place}
                        
추가정보 : ${event.detail}
   
아래 링크를 클릭하면 초대를 수락하게 됩니다.
수락하시겠습니까?
${link}
            """.trimIndent(),
            link = Link(
                webUrl = "https://plantogethers.page.link/qL6j",
                mobileWebUrl = "https://plantogethers.page.link/qL6j"
            )
        )
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

    private fun getLink() : String {
        val inviteLink = "https://plantogethers.page.link/invite?inviter=${userName}&&id=${id}"
        var result = ""
        val dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse(inviteLink))
            .setDomainUriPrefix("https://plantogethers.page.link")
            .setAndroidParameters(
                AndroidParameters.Builder().build()
            )

        dynamicLink.buildShortDynamicLink()
            .addOnSuccessListener {
                Log.d("dynamic1", it.shortLink.toString())
                result = it.shortLink.toString()
                sendMessage(result)

            }
            .addOnCompleteListener {
                Log.d("dynamic2", it.toString())
            }
        return result
    }

    private fun getEventInfo() {
        rdb = Firebase.database.getReference("$userName/Events")
        rdb.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                event = snapshot.getValue(EventData::class.java)!!
                event?.let {
                    // event 데이터 사용
                    CoroutineScope(Dispatchers.IO).launch {
                        withContext(Dispatchers.Main) {
                            text = "${userName}님이 [${event.title}]\n" +
                                    "이벤트에 초대했습니다.\n\n" +
                                    "일시 : ${event.date}\n\n" +
                                    "장소 : ${event.place}\n\n" +
                                    "추가정보 : ${event.detail}\n\n"
                            initLayout()
                            initBtn()
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // 처리 실패 시 호출되는 메서드
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_EVENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val dataChanged = data?.getBooleanExtra("dataChanged", false) ?: false
            if (dataChanged) {
                userName = data?.getStringExtra("userName").toString()
                id = data?.getStringExtra("id").toString()
                getEventInfo()
            }
        }
    }
}

