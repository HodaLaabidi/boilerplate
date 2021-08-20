package com.boilerplate.data.repositories


import com.google.gson.Gson
import com.google.gson.JsonObject
import com.boilerplate.data.db.AppDatabase
import com.boilerplate.data.db.entities.User
import com.boilerplate.data.network.MyApi
import com.boilerplate.data.network.SafeApiRequest
import com.boilerplate.data.network.responses.AuthResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class UserRepository(
    private val api: MyApi,
    private val db: AppDatabase
) : SafeApiRequest() {


    fun cleardb() {
        db.clearAllTables()
    }

    suspend fun getUserToken() = db.getUserDao().getToken()

    suspend fun saveUser(user: User) = db.getUserDao().upsert(user)

    fun getUser() = db.getUserDao().getuser()


    suspend fun userLogin(postParams: JsonObject): AuthResponse {
        return apiRequest { api.userLogin(postParams!!) }
    }

    suspend fun userLoginUsingSocial(postParams: JsonObject): AuthResponse {
        return apiRequest { api.userLoginUsingSocial(postParams!!) }
    }

    suspend fun userSignupUsingSocial(postParams: JsonObject): AuthResponse {
        return apiRequest { api.userSignupUsingSocial(postParams!!) }
    }

    suspend fun userSignup(
        postParams: JsonObject
    ): AuthResponse {
        return apiRequest { api.userSignup(postParams!!) }
    }


        fun updateProfilePicture(url: String) = db.getUserDao().updateProfilePicture(url)
        fun updateProfileDetails(
            firstName: String,
            gender: Int,
            lastName: String,
            telNumber: String
        ) = db.getUserDao().updateProfileDetails(firstName, gender, lastName, telNumber)


}