package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowInsetsAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpSignUpBtn.setOnClickListener {
            signUp()
        }
    }

    private fun signUp() {
        val nickname = binding.signUpNameEt.text.toString()
        val email = binding.signUpIdEt.text.toString() + "@" + binding.signUpDirectInputEt.text.toString()
        val password = binding.signUpPasswordEt.text.toString()
        val passwordCheck = binding.signUpPasswordCheckEt.text.toString()

        if (nickname.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "빈칸을 모두 입력하세요", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != passwordCheck) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
            return
        }

        // RoomDB 저장
        val userDB = SongDatabase.getInstance(this)!!
        userDB.userDao().insert(User(email, password))

        // API 요청
        val signUpRequest = SignUpRequest(nickname, email, password)
        val onboardingService = NetworkModule.getClient().create(OnboardingService::class.java)
        val call = onboardingService.signUp(signUpRequest)

        call.enqueue(object : Callback<JoinResponse> {
            override fun onResponse(call: Call<JoinResponse>, response: Response<JoinResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@SignUpActivity, "회원가입 성공!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@SignUpActivity, "이미 있는 유저입니다 ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JoinResponse>, t: Throwable) {
                Log.e("SignUpError", "회원가입 실패: ${t.message}", t)
                Toast.makeText(this@SignUpActivity, "네트워크 실패: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}


