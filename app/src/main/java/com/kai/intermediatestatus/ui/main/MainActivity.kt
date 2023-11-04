package com.kai.intermediatestatus.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kai.intermediatestatus.R
import com.kai.intermediatestatus.data.api.Result
import com.kai.intermediatestatus.databinding.ActivityMainBinding
import com.kai.intermediatestatus.ui.ViewModelFactory
import com.kai.intermediatestatus.ui.add.AddStoryActivity
import com.kai.intermediatestatus.ui.menu.StartMenuActivity

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: MainAdapter
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val layoutManager = LinearLayoutManager(this)
        binding.rvMain.layoutManager = layoutManager

       viewModel.getSession().observe(this){ result ->
           if (!result.isLogin){
               startActivity(Intent(this, StartMenuActivity::class.java))
               finish()
           } else {
               viewModel.getStories(result.token)
           }
       }

        setData()
        setupView()
        toAddStory()
    }

    private fun setData(){
        viewModel.listStory.observe(this){
            when (it) {
                is Result.Loading ->{
                    showLoading(true)
                }
                is Result.Error ->{
                    showLoading(false)
                }
                is Result.Success ->{
                    showLoading(false)
                    adapter  = MainAdapter(it.data)
                    binding.rvMain.adapter = adapter
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvMain.visibility = if (isLoading) View.GONE else View.VISIBLE
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

    private fun toAddStory(){
        binding.addStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_Logout -> {
                viewModel.logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}