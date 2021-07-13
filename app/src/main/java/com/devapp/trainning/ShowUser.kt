package com.devapp.trainning

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.devapp.trainning.databinding.ActivityShowUserBinding
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowUser : AppCompatActivity() {
    private lateinit var _binding:ActivityShowUserBinding
    private lateinit var adapter: ShowPostAdapter
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityShowUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFlagFullScreen()
        adapter = ShowPostAdapter()
        binding.recycleView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.recycleView.adapter = SlideInLeftAnimationAdapter(AlphaInAnimationAdapter(ScaleInAnimationAdapter(adapter)))
        RetrofitInstance.getInstance().getApiService().getListPost().enqueue(object:Callback<List<Post>>{
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if(response.body()!=null){
                    val listData = response.body()
                    if(response.isSuccessful)
                        listData?.let { adapter.setData(it) }
                    else Toast.makeText(this@ShowUser, "${response.errorBody()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Toast.makeText(this@ShowUser, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        setFlagFullScreen()
    }

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