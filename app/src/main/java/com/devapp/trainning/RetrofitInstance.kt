package com.devapp.trainning

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance private constructor() {
    object Holder{
        val instance = RetrofitInstance()
    }
    companion object{
        fun getInstance():RetrofitInstance{
            return Holder.instance
        }
    }
    private val gson = GsonConverterFactory.create()
    private val retrofit = Retrofit.Builder().baseUrl("https://jsonplaceholder.typicode.com/").addConverterFactory(gson).build()
    fun getApiService() = retrofit.create(ApiService::class.java)
}