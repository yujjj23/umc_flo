package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()  // 액션바 숨기기
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginsignUpTextButton.setOnClickListener {
            // 로그인 처리 로직
            startActivity(Intent(this,SignUpActivity::class.java))
        }

        binding.SignuploginButton.setOnClickListener{
            login()
        }
    }

    private fun login(){
        if(binding.logInIdEt.text.toString().isEmpty() || binding.logInDirectInputEt.text.toString().isEmpty()){
            Toast.makeText(this,"이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        if(binding.logInPasswordEt.text.toString().isEmpty() ){
            Toast.makeText(this,"비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        val email: String=binding.logInIdEt.text.toString()+ "@" +binding.logInDirectInputEt.text.toString()
        val pwd: String=binding.logInPasswordEt.text.toString()

        val songDB=SongDatabase.getInstance(this)!!
        val user=songDB.userDao().getUser(email, pwd)

        user?.let {
            Log.d("LOGIN_ACT/GET_USER","userId:${user.id},$user")
            saveJwt(user.id)
            Toast.makeText(this, "로그인되었습니다", Toast.LENGTH_SHORT).show()  // 로그인 성공 메시지
            startMainActivity()
            return  // 로그인 성공 후 추가적인 코드 실행 방지
        }
        Toast.makeText(this, "회원 정보가 존재하지 않습니다",Toast.LENGTH_SHORT).show()

    }

    private fun saveJwt(jwt:Int){
        val spf=getSharedPreferences("auth", MODE_PRIVATE)
        val editor=spf.edit()

        editor.putInt("jwt",jwt)
        editor.apply()
    }

    private fun startMainActivity(){
        val intent=Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
