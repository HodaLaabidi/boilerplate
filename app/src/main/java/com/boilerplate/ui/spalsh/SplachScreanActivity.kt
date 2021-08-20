package com.boilerplate.ui.spalsh

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.boilerplate.R
import com.boilerplate.data.db.entities.User
import com.boilerplate.databinding.ActivitySplashScreenBinding
import com.boilerplate.ui.BaseActivity
import com.boilerplate.ui.auth.AuthListener
import com.boilerplate.ui.auth.AuthViewModel
import com.boilerplate.ui.auth.AuthViewModelFactory
import com.boilerplate.ui.home.HomeActivity
import org.kodein.di.generic.instance


class SplachScreanActivity : BaseActivity() , AuthListener {

    private val factory : AuthViewModelFactory by instance()
    lateinit var viewModel : AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySplashScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)
        binding.viewmodel = viewModel
        viewModel.authListener = this
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.getLoggedInUser().observe(this) { user->
                if (user == null){
                    Intent(this@SplachScreanActivity, OnBoardingActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                }else{
                    onSuccess()
                }
            }
        }, 3000)



    }

    override fun onStarted() {

    }

    override fun onSuccess(user: User) {

    }

    override fun onSuccess() {
        Intent(this@SplachScreanActivity, HomeActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }


    override fun onFailure(message: String) {
        Intent(this@SplachScreanActivity, HomeActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }
}
