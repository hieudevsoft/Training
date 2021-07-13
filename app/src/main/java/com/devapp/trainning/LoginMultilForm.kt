package com.devapp.trainning

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.core.widget.addTextChangedListener
import com.devapp.trainning.databinding.ActivityLoginMultilFormBinding

class LoginMultilForm : AppCompatActivity() {
    private lateinit var _binding: ActivityLoginMultilFormBinding
    private val USERNAME_PATTERN = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$"
    private val PHONE_NUMBER_PATTERN = "^\\d{10}$"
    private var isPassOk = false
    private var isUsernamePhoneOk = false
    private val binding get() = _binding!!
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginMultilFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFlagFullScreen()
        binding.showError = false
        binding.backGroundButton = false
        binding.edtUserPhone.addTextChangedListener {
            val text = it.toString()
            if (text.isNotEmpty()) {
                if (!isValidPhone(text) && !isValidUser(text)) {
                    binding.showError = true
                    binding.errorText = "User name hoặc số điện thoại không đúng"
                    isUsernamePhoneOk = true
                    binding.edtUserPhone.setBackgroundResource(R.drawable.custom_login_edt_error)
                } else {
                    binding.edtUserPhone.setBackgroundResource(R.drawable.custom_login_edt_event)
                    binding.showError = false
                    binding.errorText = ""
                    binding.backGroundButton = isPassOk
                }
            } else isUsernamePhoneOk = false
            binding.btnLogin.isEnabled = isPassOk && isUsernamePhoneOk
        }
        binding.edtPassword.addTextChangedListener {
            val text = it.toString()
            if (text.isNotEmpty()) {
                if (!isValidPass(text)) {
                    binding.showError = true
                    binding.errorText = "Mật khẩu phải trên 6 ký tự"
                    isPassOk = true
                    binding.edtPassword.setBackgroundResource(R.drawable.custom_login_edt_error)
                } else {
                    binding.edtPassword.setBackgroundResource(R.drawable.custom_login_edt_event)
                    binding.showError = false
                    binding.errorText = ""
                    binding.backGroundButton = isUsernamePhoneOk
                }
            } else isPassOk = false
            binding.btnLogin.isEnabled = isPassOk && isUsernamePhoneOk
        }
        binding.btnLogin.setOnClickListener {  }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        setFlagFullScreen()
    }


    private fun isValidUser(username: String): Boolean {
        return username.matches(USERNAME_PATTERN.toRegex())
    }

    private fun isValidPhone(phone: String): Boolean {
        return phone.matches(PHONE_NUMBER_PATTERN.toRegex())
    }

    private fun isValidPass(password: String): Boolean = password.length >= 6

    private fun setFlagFullScreen(){
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            if(Build.VERSION.SDK_INT<= Build.VERSION_CODES.R)
                window.decorView.apply {
                    systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
                }
            else{
                val windowController = window.insetsController
                windowController?.hide(WindowInsets.Type.statusBars())
                windowController?.hide(WindowInsets.Type.navigationBars())
                windowController?.hide(WindowInsets.Type.captionBar())
                windowController?.hide(WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_SWIPE)
        }
    }
}