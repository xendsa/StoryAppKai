package com.kai.intermediatestatus.ui.detail

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kai.intermediatestatus.data.response.ListStoryItem
import com.kai.intermediatestatus.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var storyItem: ListStoryItem
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        storyItem = if (Build.VERSION.SDK_INT >= 33){
            intent.getParcelableExtra("storyItem", ListStoryItem::class.java)!!
        }else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("storyItem")!!
        }
        setData()
        setupView()
    }
    @Suppress("DEPRECATION")
    private fun setupView(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.show()
    }

    @Suppress("DEPRECATION")
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setData(){
        Glide
            .with(this)
            .load(storyItem.photoUrl)
            .fitCenter()
            .into(binding.ivDetailImage)

        binding.tvName.text = storyItem.name
        binding.tvDescription.text = storyItem.description
    }
}