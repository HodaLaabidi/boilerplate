package com.boilerplate.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_USER_ID = 0

@Entity
data class User(
    var token: String? = null,
    var id: String? = null,
    var firstname: String? = null,
    var lastname: String? = null,
    var phone: String? = null,
    var gender: Int = 0,
    var avatar: String? = null,
    var email: String? = null,
){
    @PrimaryKey(autoGenerate = false)
    var uid: Int = CURRENT_USER_ID
}