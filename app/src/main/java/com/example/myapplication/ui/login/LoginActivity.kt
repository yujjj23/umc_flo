package com.example.myapplication.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.ui.main.MainActivity
import com.example.myapplication.ui.utils.NetworkModule
import com.example.myapplication.data.remote.OnboardingService
import com.example.myapplication.data.remote.TestResponse
import com.example.myapplication.data.remote.TestService
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.ui.signup.SignUpActivity
import com.example.myapplication.ui.song.SongDatabase
import com.example.myapplication.ui.utils.AuthNetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError


class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginsignUpTextButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.SignuploginButton.setOnClickListener {
            login()
        }

        binding.loginSocialLoginLayout.setOnClickListener {
            kakaoLogin()
        }
    }

    // 이메일+비번 로컬 및 서버 로그인
    private fun login() {
        val idPart = binding.logInIdEt.text.toString()
        val domainPart = binding.logInDirectInputEt.text.toString()
        val password = binding.logInPasswordEt.text.toString()

        if (idPart.isEmpty() || domainPart.isEmpty()) {
            Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        val email = "$idPart@$domainPart"
        val songDB = SongDatabase.getInstance(this)!!
        val user = songDB.userDao().getUser(email, password)

        if (user != null) {
            Log.d("LOGIN_ACT/ROOM_USER", "userId: ${user.id}, $user")
            saveJwt(user.id)
            Toast.makeText(this, "로컬 로그인 성공!", Toast.LENGTH_SHORT).show()
            startMainActivity()
            return
        }

        val loginRequest = LoginRequest(email, password)
        val service = NetworkModule.getClient().create(OnboardingService::class.java)
        service.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.isSuccess == true) {
                        val memberId = body.result?.memberId ?: 0
                        val token = body.result?.accessToken ?: ""

                        saveJwt(memberId)
                        saveAccessToken(token)

                        Toast.makeText(this@LoginActivity, "서버 로그인 성공!", Toast.LENGTH_SHORT).show()
                        startMainActivity()
                    } else {
                        Toast.makeText(this@LoginActivity, body?.message ?: "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    when (response.code()) {
                        401 -> Toast.makeText(this@LoginActivity, "계정이 존재하지 않거나 비밀번호 오류입니다", Toast.LENGTH_SHORT).show()
                        else -> Toast.makeText(this@LoginActivity, "서버 오류: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("LOGIN_ACT/API_ERROR", t.message ?: "unknown error")
            }
        })
    }

    private fun saveJwt(jwt: Int) {
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()
        editor.putInt("jwt", jwt)
        editor.apply()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun saveAccessToken(token: String) {
        AuthNetworkModule.setAccessToken(token)
        Log.d("LOGIN_ACT/TOKEN", "Access token 저장 완료: $token")

        val service = AuthNetworkModule.getClient().create(TestService::class.java)
        service.testApi().enqueue(object : Callback<TestResponse> {
            override fun onResponse(call: Call<TestResponse>, response: Response<TestResponse>) {
                if (response.isSuccessful) {
                    Log.d("TEST_API", "성공: ${response.body()}")
                } else {
                    Log.e("TEST_API", "실패 코드: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<TestResponse>, t: Throwable) {
                Log.e("TEST_API", "오류: ${t.message}")
            }
        })
    }

    // 카카오 로그인 (scopes 없이 기본 로그인)
    private fun kakaoLogin() {
        UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
            if (error != null) {
                Toast.makeText(this, "카카오 로그인 실패: ${error.message}", Toast.LENGTH_SHORT).show()
            } else if (token != null) {
                requestUserInfo()  // 로그인 성공 후 사용자 정보 요청
            }
        }
    }

    // 사용자 정보 요청
    private fun requestUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Toast.makeText(this, "사용자 정보 요청 실패: ${error.message}", Toast.LENGTH_SHORT).show()
            } else if (user != null) {
                val nickname = user.kakaoAccount?.profile?.nickname ?: "닉네임 없음"
//                val email = user.kakaoAccount?.email ?: "이메일 없음"
                val profileImageUrl = user.kakaoAccount?.profile?.thumbnailImageUrl ?: "프로필 사진 없음"

                saveKakaoLogin(true)
                Toast.makeText(this, "닉네임: $nickname\n프로필 사진 URL: $profileImageUrl", Toast.LENGTH_LONG).show()
                startMainActivity()
            }
        }

    }

    // 추가 권한 요청 (필요하면 호출해서 사용)
    private fun requestAdditionalScopes() {
        val scopes = listOf("account_email")  // 요청할 추가 동의 권한
        UserApiClient.instance.loginWithNewScopes(this, scopes) { token, error ->
            if (error != null) {
                Toast.makeText(this, "추가 동의 실패: ${error.message}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "추가 동의 성공", Toast.LENGTH_SHORT).show()
                requestUserInfo()  // 다시 사용자 정보 요청
            }
        }
    }

    private fun saveKakaoLogin(isLoggedIn: Boolean) {
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()
        editor.putBoolean("kakaoLogin", isLoggedIn)
        editor.apply()
    }

    private fun kakaoLogout() {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Toast.makeText(this, "로그아웃 실패: ${error.message}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                // 로그아웃 후 로그인 화면 이동 등 처리
            }
        }
    }
}


