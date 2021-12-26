package com.praeter.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.praeter.data.IRepository
import com.praeter.data.remote.dto.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.and
import timber.log.Timber
import java.io.UnsupportedEncodingException
import java.lang.StringBuilder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    private val shouldShowHideProgressBar: MutableLiveData<Boolean> = MutableLiveData()
    private val onLoginSuccessful: MutableLiveData<Boolean> = MutableLiveData()
    private val onLoginFailed: MutableLiveData<Boolean> = MutableLiveData()

    fun getShouldDisplayHideProgressBar(): LiveData<Boolean> {
        return shouldShowHideProgressBar
    }

    fun getOnLoginSuccessful(): LiveData<Boolean> {
        return onLoginSuccessful
    }

    fun getOnLoginFailed(): LiveData<Boolean> {
        return onLoginFailed
    }


    fun makeCallLogin(user: User) {
        shouldShowHideProgressBar.value = true

        // TODO : Make real REST api call
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Timber.d("viewModelScope.launch(Dispatchers.IO) - make rest call login")
                val dbResponse = repository.dbConnection()
                Timber.e("db message : %s", dbResponse.toString())


                if (0 == dbResponse.code) {

                    val loginResponse = repository.loginUser(user)

                    if (0 == loginResponse.code) {
                        Timber.e("login message : %s", loginResponse.toString())
                        withContext(Dispatchers.Main) {
                            onLoginSuccessful.value = true
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            onLoginFailed.value = true
                        }
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }

    }


    private fun convertToHex(data: ByteArray): String? {
        val buf = StringBuilder()
        for (b in data) {
            var halfbyte: Int = b.ushr(4) and 0x0F
            var two_halfs = 0
            do {
                buf.append(if (halfbyte in 0..9) ('0'.toInt() + halfbyte).toChar() else ('a'.toInt() + (halfbyte - 10)).toChar())
                halfbyte = b and 0x0F
            } while (two_halfs++ < 1)
        }
        return buf.toString()
    }

    @Throws(NoSuchAlgorithmException::class, UnsupportedEncodingException::class)
    fun SHA1(text: String): String? {
        val md: MessageDigest = MessageDigest.getInstance("SHA-1")
        val textBytes = text.toByteArray(charset("iso-8859-1"))
        md.update(textBytes, 0, textBytes.size)
        val sha1hash: ByteArray = md.digest()
        return convertToHex(sha1hash)
    }
}