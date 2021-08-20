package com.boilerplate.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.boilerplate.data.db.entities.CURRENT_USER_ID
import com.boilerplate.data.db.entities.User

@Dao
interface UserDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: User) : Long

    @Query("SELECT * FROM user WHERE uid = $CURRENT_USER_ID")
    fun getuser() : LiveData<User>

    @Query("SELECT token FROM user WHERE uid = $CURRENT_USER_ID")
    suspend fun getToken() : String?

    @Query("UPDATE user SET avatar=:url WHERE uid = $CURRENT_USER_ID")
    fun updateProfilePicture(url: String) : Int

    @Query("UPDATE user SET firstname=:firstName , lastname =:lastName , gender=:gender , phone=:telNumber WHERE uid = $CURRENT_USER_ID")
    fun updateProfileDetails(firstName: String, gender: Int, lastName: String, telNumber: String) : Int

}