package com.example.plantogether.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.plantogether.databinding.ActivityLoginBinding

import com.example.plantogether.roomDB.Event
import com.example.plantogether.roomDB.EventDatabase
import com.example.plantogether.roomDB.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.kakao.util.maps.helper.Utility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    companion object {
        const val TAG = "LoginActivity"
    }

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    lateinit var db: EventDatabase
    lateinit var rdb: DatabaseReference
    lateinit var userName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var keyHash = Utility.getKeyHash(this)
        Log.d("key확인", keyHash.toString())

        setContentView(binding.root)
        db = EventDatabase.getDatabase(this)
        initBtn()
    }

    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
            saveUserInfo()
        }
        // val intent = Intent(this@LoginActivity, MainActivity::class.java)
        // startActivity(intent)
    }




    private fun initBtn() {
        binding.run {
            loginBtn.setOnClickListener {
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity)) {
                    UserApiClient.instance.loginWithKakaoTalk(
                        this@LoginActivity,
                        callback = callback
                    )
                } else {
                    UserApiClient.instance.loginWithKakaoAccount(
                        this@LoginActivity,
                        callback = callback
                    )
                }
            }
        }
    }

    private fun saveUserInfo() {
        UserApiClient.instance.me { user, error ->
            userName = user?.kakaoAccount?.profile?.nickname.toString()
            rdb = Firebase.database.getReference("$userName/User")
            insertDB(user?.kakaoAccount?.profile?.nickname.toString())

            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                .apply {
                putExtra("userName", userName)
            }
            // println(userName + " 데이터 Main으로 전송 완료")
            startActivity(intent)
        }
    }

    private fun insertDB(nickname: String) {
        val users = User(nickname)
        CoroutineScope(Dispatchers.IO).launch {
            db.eventDao().insertUser(users)
            // Firebase에 유저정보 추가, 일단은 키와 밸류값 모두 카카오톡 사용자명으로 해둔 상태
            rdb.child(nickname).setValue(nickname)
        }
    }
}