package com.devapp.trainning

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("/posts")
    fun getListPost(): Call<List<Post>>

}