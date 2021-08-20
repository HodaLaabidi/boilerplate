package com.boilerplate.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.boilerplate.R
import kotlinx.android.synthetic.main.activity_login.*
import com.boilerplate.data.db.entities.User
import com.boilerplate.databinding.ActivitySignupBinding
import com.boilerplate.ui.BaseActivity
import com.boilerplate.util.hide
import com.boilerplate.util.show
import com.boilerplate.util.snackbar
import com.boilerplate.ui.home.HomeActivity
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class SignupActivity : BaseActivity(), AuthListener, KodeinAware {

    private val factory : AuthViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding: ActivitySignupBinding
                = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        val viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)
        binding.viewmodel = viewModel

        viewModel.authListener = this

        viewModel.getLoggedInUser().observe(this, Observer { user ->
            if(user != null){
                Intent(this, HomeActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
        })

    }


    override fun onStarted() {
        progress_bar.show()
    }

    override fun onSuccess(user: User) {
        progress_bar.hide()
    }

    override fun onFailure(message: String) {
        progress_bar.hide()
        root_layout.snackbar(message)
    }
}
