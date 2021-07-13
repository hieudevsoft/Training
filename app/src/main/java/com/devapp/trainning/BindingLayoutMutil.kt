package com.devapp.trainning

import android.widget.Button
import androidx.databinding.BindingAdapter

object BindingLayoutMutil {
    @BindingAdapter("loadBackgroundButton")
    @JvmStatic
    fun loadBackgroundButton(button: Button, isEnable:Boolean){
        if(isEnable)
            button.setBackgroundResource(R.drawable.custom_login_button_enabled)
        else button.setBackgroundResource(R.drawable.custom_login_button_notenabled)
    }
}