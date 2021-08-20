package com.boilerplate.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.boilerplate.R
import kotlinx.android.synthetic.main.activity_login.*
import com.boilerplate.data.db.entities.User
import com.boilerplate.databinding.ActivityLoginBinding
import com.boilerplate.ui.BaseActivity
import com.boilerplate.ui.auth.AuthViewModel.Companion.RC_GOOGLE_SIGN_IN_CODE
import com.boilerplate.ui.auth.AuthViewModel.Companion.gso
import com.boilerplate.util.hide
import com.boilerplate.util.show
import com.boilerplate.util.snackbar
import com.boilerplate.ui.home.HomeActivity
import fr.medespoir.data.const.AuthType

import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance


class LoginActivity : BaseActivity(), AuthListener, KodeinAware {

    private val factory : AuthViewModelFactory by instance()
    var auth: FirebaseAuth? = null
    private companion object {
        private val facebook_permissions = mutableListOf("email", "public_profile")
    }
    lateinit var viewModel : AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)
        binding.viewmodel = viewModel

        viewModel.authListener = this

        viewModel.getLoggedInUser().observe(this, Observer { user->
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

    private fun doSocialAuth(authType: AuthType, view : View) {
        when (authType) {
            AuthType.GOOGLE -> viewModel.googleSignIn().also {
                startActivityForResult(it, RC_GOOGLE_SIGN_IN_CODE)
            }
            AuthType.FACEBOOK -> {
                viewModel.loginManager.logInWithReadPermissions(this, facebook_permissions)
            }
            AuthType.EMAIL -> {
                //viewModel.fetchAllProviderForEmail(authType.authValue)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.handleOnActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateUser(user: FirebaseUser?) {
        Intent(this,SignupActivity::class.java).also {
            it.putExtra("userData","fromLogin")
            startActivity(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        GoogleSignIn.getClient(this, gso).signOut()
        LoginManager.getInstance().logOut()

    }
}
