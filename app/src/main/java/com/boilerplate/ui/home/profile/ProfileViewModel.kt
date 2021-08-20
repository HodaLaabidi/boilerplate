package com.boilerplate.ui.home.profile

import androidx.lifecycle.ViewModel;
import com.boilerplate.data.repositories.UserRepository

class ProfileViewModel(
    repository: UserRepository
) : ViewModel() {

    val user = repository.getUser()

}
