package com.kai.intermediatestatus.ui.main

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kai.intermediatestatus.ui.add.AddStoryActivity

class MainViewModel(private val context: Context) : ViewModel() {
    fun fabClick() {
        val intent = Intent(context, AddStoryActivity::class.java)
        context.startActivity(intent)
    }
    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(context) as T
        }
    }
}
