package com.boilerplate.ui.auth

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.boilerplate.MVVMApplication
import com.boilerplate.data.db.entities.User
import com.boilerplate.data.repositories.UserRepository
import com.boilerplate.util.ApiException
import com.boilerplate.util.Coroutines
import com.boilerplate.util.NoInternetException
import com.boilerplate.util.isValidEmail
import fr.medespoir.util.await
import java.util.HashMap


class AuthViewModel(
    private val repository: UserRepository,
    private val application : com.boilerplate.MVVMApplication
) : ViewModel() {

    var name: String? = null
    var email: String? = null
    var password: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var phoneNumber: String? = null
    var gender: Int = 0
    var countryCode : String? = null
    var authListener: AuthListener? = null
    var imageUrl : String? = null
    var socialId : String? = null
    var isSocialSignUp = false


    companion object {
        private const val GOOGLE_PRIVATE_CLIENT_ID =
            "858065912686-7i8rjrfj6d72ba7ueh9cmomi5d1erl74.apps.googleusercontent.com"   // change it with your google private client id
        const val RC_GOOGLE_SIGN_IN_CODE = 2555
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(GOOGLE_PRIVATE_CLIENT_ID)
            .requestEmail()
            .build()
    }





    private val mGoogleSignClient by lazy {
        GoogleSignIn.getClient(application, gso)
    }

    fun googleSignIn() = mGoogleSignClient.signInIntent

    fun getLoggedInUser() = repository.getUser()


    fun onSignUpButtonClick(){
        authListener?.onStarted()
        if ( !isValidEmail(email) ) {
            authListener?.onFailure("Invalid email")
            return
        }
        if (password.isNullOrEmpty() || (password?.length!! < 6)) {
            authListener?.onFailure("Invalid password")
            return
        }

        if (firstName.isNullOrEmpty()) {
            authListener?.onFailure("Invalid first name")
            return
        }
        if (lastName.isNullOrEmpty()) {
            authListener?.onFailure("Invalid last name")
            return
        }
        if (phoneNumber.isNullOrEmpty()) {
            authListener?.onFailure("Invalid phone number")
            return
        }

        Coroutines.main {
            try {
                val postParams = JsonObject()
                postParams.addProperty("username", email!!)
                postParams.addProperty("email", email!!)
                postParams.addProperty("password", password!!)
                postParams.addProperty("name", firstName!!)
                postParams.addProperty("secondeName", lastName!!)
                postParams.addProperty("phone", countryCode+phoneNumber!!)
                postParams.addProperty("gender", gender)
                postParams.addProperty("socialID", socialId)
                val authResponse = repository.userSignup(postParams)
                authResponse.data?.let {
                    var user = Gson().fromJson(Gson().toJson(it), User::class.java)
                    saveUserData(user.id)
                    login()
                    //authListener?.onSuccess(user!!)
                    return@main
                } ?: kotlin.run {
                    authResponse.message?.let { authListener?.onFailure(it) }
                    return@main
                }
            } catch (e: ApiException) {
                authListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                authListener?.onFailure(e.message!!)
            }
        }

    }

    fun onSignSocialClick(){
        authListener?.onStarted()
        if ( !isValidEmail(email) ) {
            authListener?.onFailure("Invalid email")
            return
        }
        if (password.isNullOrEmpty() || (password?.length!! < 6)) {
            authListener?.onFailure("Invalid password")
            return
        }
        if (firstName.isNullOrEmpty()) {
            authListener?.onFailure("Invalid first name")
            return
        }
        if (lastName.isNullOrEmpty()) {
            authListener?.onFailure("Invalid last name")
            return
        }
        if (phoneNumber.isNullOrEmpty()) {
            authListener?.onFailure("Invalid phone number")
            return
        }
        Coroutines.main {
            try {
                val postParams = JsonObject()
                postParams.addProperty("username", email!!)
                postParams.addProperty("email", email!!)
                postParams.addProperty("firstname", firstName!!)
                postParams.addProperty("lastname", lastName!!)
                postParams.addProperty("phone", countryCode+phoneNumber!!)
                postParams.addProperty("gender", gender)
                postParams.addProperty("imageUrl", imageUrl?.let { it } ?: "")
                postParams.addProperty("socialID", socialId)

                // if is connected using Social Media

                val authResponse = repository.userSignupUsingSocial(postParams)
                authResponse.data?.let {
                    var user = Gson().fromJson(Gson().toJson(it), User::class.java)
                    saveUserData(user.id)
                    loginSocialMedia(FirebaseAuth.getInstance().currentUser)
                    return@main
                } ?: kotlin.run {
                    authListener?.onFailure("")
                    return@main
                }
            } catch (e: ApiException) {
                authListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                authListener?.onFailure(e.message!!)
            }
        }

    }

    fun onOtpButtonClick(view: View) {
        authListener?.onStarted()
        if ( !isValidEmail(email) ) {
            authListener?.onFailure("Invalid email")
            return
        }
        if (password.isNullOrEmpty() || (password?.length!! < 6)) {
            authListener?.onFailure("Invalid password")
            return
        }
        if (firstName.isNullOrEmpty()) {
            authListener?.onFailure("Invalid first name")
            return
        }
        if (lastName.isNullOrEmpty()) {
            authListener?.onFailure("Invalid last name")
            return
        }
        if (phoneNumber.isNullOrEmpty()) {
            authListener?.onFailure("Invalid phone number")
            return
        }
        if (isSocialSignUp){
            authListener?.onSuccess(User("","",firstName,lastName,countryCode+phoneNumber,gender,"",email))
        }else{
            Coroutines.main {
                createUserWithEmailAndPassword()
            }
        }




    }

    fun login(){
        Coroutines.main {
            try {
                val postParams = JsonObject()
                postParams.addProperty("password", password!!)
                postParams.addProperty("email", email!!)
                val authResponse = repository.userLogin(postParams)
                authResponse.data?.let {
                    var user = Gson().fromJson(Gson().toJson(it), User::class.java)
                    try {
                        connectToFirebase()
                    }catch (e:Exception){

                    }
                    authListener?.onSuccess(user)
                    repository.saveUser(user)
                    return@main
                } ?: kotlin.run {
                    authListener?.onFailure(authResponse.message!!)
                }
            } catch (e: ApiException) {
                authListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                authListener?.onFailure(e.message!!)
            }
        }
    }

    fun loginSocialMedia(user: FirebaseUser?) {
        Coroutines.main {
            try {
                val postParams = JsonObject()
                postParams.addProperty("socialID", user?.uid) // F ou G
                postParams.addProperty("email", user?.email)
                val authResponse = repository.userLoginUsingSocial(postParams)
                when (authResponse.status) {
                    "0" -> {
                        authListener?.onCreateUser(user)
                        return@main
                    }
                    "1" -> {
                        authResponse.data?.let {
                            var user = Gson().fromJson(Gson().toJson(it), User::class.java)
                            authListener?.onSuccess(user)
                            repository.saveUser(user)
                            return@main
                        }
                    }
                    else -> {
                        authListener?.onFailure(authResponse.message!!)
                        return@main
                    }
                }
            } catch (e: ApiException) {
                authListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                authListener?.onFailure(e.message!!)
            }
        }
    }

  /*  fun resetPassword(view: View){
        authListener?.onStarted()
        if ( !isValidEmail(email) ) {
            authListener?.onFailure("Invalid email")
            return
        }
        Coroutines.main {
            try {
                val postParams = JsonObject()
                postParams.addProperty("email", email!!)
                val authResponse = repository.resetPassword(postParams)
                authResponse?.let {
                    if (authResponse.status == "1")
                        authListener?.onSuccess()
                    else  authListener?.onFailure(authResponse.message!!)
                    return@main
                }
            } catch (e: ApiException) {
                authListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                authListener?.onFailure(e.message!!)
            }
        }
    } */

    fun onLoginButtonClick(view: View) {
        authListener?.onStarted()
        if (!isValidEmail(email)) {
            authListener?.onFailure("Invalid email")
            return
        }

        if (password.isNullOrEmpty() || (password?.length!! < 6)) {
            authListener?.onFailure("Invalid password")
            return
        }

        login()

    }


    fun onSignup(view: View) {
        Intent(view.context, SignupActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

    fun handleOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_GOOGLE_SIGN_IN_CODE && data != null) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
            }
        } else if (mCallbackManager.onActivityResult(requestCode, resultCode, data))
            println("Result should be handled")
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        var auth = FirebaseAuth.getInstance()
        auth.signInWithCredential(credential).addOnCompleteListener {task->
            if (task.isSuccessful) {
                val user = auth.currentUser
                loginSocialMedia(user)
            } else {
                authListener?.onFailure(task.exception!!.localizedMessage)
            }
        }
    }

    val loginManager: LoginManager = LoginManager.getInstance()
    private val mCallbackManager = CallbackManager.Factory.create()
    private val mFacebookCallback = object : FacebookCallback<LoginResult> {
        override fun onSuccess(result: LoginResult?) {
            handleFacebookAccessToken(result?.accessToken!!)
        }

        override fun onCancel() {
            authListener?.onFailure("Cancel")
        }

        override fun onError(error: FacebookException?) {
            Log.d("error",error!!.localizedMessage)
            authListener?.onFailure(error!!.localizedMessage)

        }
    }

    init {
        loginManager.registerCallback(mCallbackManager, mFacebookCallback)
    }

    // [START auth_with_facebook]
    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        var auth = FirebaseAuth.getInstance()
        auth.signInWithCredential(credential).addOnCompleteListener {task->
            if (task.isSuccessful) {
                val user = auth.currentUser
                loginSocialMedia(user)
            } else {
                authListener?.onFailure(task.exception!!.localizedMessage)
            }
        }
    }

    private suspend fun connectToFirebase() {
        var auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email,password).await()
    }
    private suspend fun createUserWithEmailAndPassword() {
        var auth = FirebaseAuth.getInstance()
        try {
            auth.createUserWithEmailAndPassword(email,password).await()
            socialId = auth.currentUser.uid
            authListener?.onSuccess(User("","",firstName,lastName,countryCode+phoneNumber,gender,"",email))
        }catch (e : FirebaseAuthUserCollisionException){
            authListener?.onFailure(e.localizedMessage)
            return
        }
    }

    private suspend fun saveUserData(idUser: String?){
        var auth = FirebaseAuth.getInstance()
        val firebaseUser = auth.currentUser
        val userId = firebaseUser.uid
        var reference = FirebaseDatabase.getInstance().getReference("Users").child(userId)
        val hashMap = HashMap<String, String>()
        hashMap["id"] = userId
        hashMap["username"] = "${this.firstName} ${this.lastName}"
        hashMap["role"] = "1"
        hashMap["id_plateform"] = idUser!!
        reference.setValue(hashMap).await()
    }

}