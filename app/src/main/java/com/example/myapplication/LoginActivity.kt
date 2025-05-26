package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()  // 액션바 숨기기
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginsignUpTextButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.SignuploginButton.setOnClickListener {
            login()
        }
    }

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

        // 1. RoomDB 확인
        val songDB = SongDatabase.getInstance(this)!!
        val user = songDB.userDao().getUser(email, password)

        if (user != null) {
            Log.d("LOGIN_ACT/ROOM_USER", "userId: ${user.id}, $user")
            saveJwt(user.id)
            Toast.makeText(this, "로컬 로그인 성공!", Toast.LENGTH_SHORT).show()
            startMainActivity()
            return
        }

        // 2. 서버 로그인 시도
        val loginRequest = LoginRequest(email, password)
        val service = NetworkModule.getClient().create(OnboardingService::class.java)
        service.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.isSuccess == true) {
                        val memberId = body.result?.memberId ?: 0
                        val token = body.result?.accessToken ?: ""

                        saveJwt(memberId) // 또는 token 저장 (sharedPreferences로)
                        saveAccessToken(token) // ✅ 여기에 accessToken 저장!

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

        // ✅ 테스트 API 호출
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


}
