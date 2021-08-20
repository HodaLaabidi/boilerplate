package com.boilerplate.ui.auth

import com.google.firebase.auth.FirebaseUser
import com.boilerplate.data.db.entities.User

interface AuthListener {
    fun onStarted()
    fun onSuccess(){}
    fun onSuccess(user: User){}
    fun onFailure(message: String)
    fun onCreateUser(user: FirebaseUser?){}
}