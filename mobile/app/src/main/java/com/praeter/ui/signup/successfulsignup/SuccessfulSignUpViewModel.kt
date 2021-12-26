package com.praeter.ui.signup.successfulsignup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.praeter.data.IRepository
import com.praeter.data.remote.dto.ApiResponse
import com.praeter.data.remote.dto.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SuccessfulSignUpViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    private val onUserSaveSuccessful: MutableLiveData<Boolean> = MutableLiveData()
    private val onUserSaveError: MutableLiveData<Boolean> = MutableLiveData()

    fun getOnUserSaveSuccessful(): LiveData<Boolean> {
        return onUserSaveSuccessful
    }

    fun getOnUserSaveError(): LiveData<Boolean> {
        return onUserSaveError
    }

    suspend fun makeRestSaveCall(user: User) {
        val apiResponse: ApiResponse = repository.saveUser(user)

        if (0 == apiResponse.code) {
            Timber.d("successful")
        } else {

            Timber.e("" + apiResponse.code)
        }
    }
}