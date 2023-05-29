package com.example.plantogether.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.plantogether.databinding.ActivityLoginBinding

import com.example.plantogether.databinding.ActivityMainBinding
import com.example.plantogether.roomDB.Username
import com.example.plantogether.roomDB.UsernameDatabase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
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

    lateinit var db: UsernameDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        var keyHash = Utility.getKeyHash(this)
//        Log.d("key확인", keyHash.toString())

        setContentView(binding.root)
        db = UsernameDatabase.getDatabase(this)
        initBtn()
    }

    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
            saveUserInfo()
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
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
            insertDB(user?.kakaoAccount?.profile?.nickname.toString())
//            user?.properties?.entries?.forEach {
//                Log.d(TAG, "showUserInfo: ${it.key} ${it.value}")
//            }
            //Log.d(TAG, "showUserInfo: ${user?.kakaoAccount?.profile?.nickname} ")
            //Log.d(TAG, "showUserInfo: ${user?.kakaoAccount?.gender} ")
        }
    }

    private fun insertDB(nickname: String) {
        val users = Username(nickname)
        CoroutineScope(Dispatchers.IO).launch {
            db.usernameDao().insertUser(users)
        }
    }
}