package com.praeter.ui.signup.premiumplan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PremiumPlanViewModel @Inject constructor(

) : ViewModel() {

    private val shouldShowHideLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val shouldEnableDisableUI: MutableLiveData<Boolean> = MutableLiveData()
    private val onCreditCardSuccessful: MutableLiveData<Boolean> = MutableLiveData()
    private val onCreditCardFailed: MutableLiveData<Boolean> = MutableLiveData()

    fun getShouldShowHideLoading(): LiveData<Boolean> {
        return shouldShowHideLoading
    }

    fun getEnableDisableUI(): LiveData<Boolean> {
        return shouldEnableDisableUI
    }

    fun getCreditCardSuccessful(): LiveData<Boolean> {
        return onCreditCardSuccessful
    }

    fun getCreditCardFailed(): LiveData<Boolean> {
        return onCreditCardFailed
    }


    fun checkCreditCardInfo(
        cardOwnerName: String, cardNumber: String,
        cardValidityMonth: String, cardValidityYear: String,
        cardCCV: String
    ) {
        shouldShowHideLoading.value = true
        if (cardOwnerName.trim { it <= ' ' }.isEmpty()
            || cardNumber.trim { it <= ' ' }.isEmpty()
            || cardValidityMonth.trim { it <= ' ' }.isEmpty()
            || cardValidityYear.trim { it <= ' ' }.isEmpty()
            || cardCCV.trim { it <= ' ' }.isEmpty()
        ) {
            Timber.e("One of field is ")
            return
        }
    }

    fun checkCardValidity() {
        shouldShowHideLoading.value = true
        shouldEnableDisableUI.value = false

        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)

            withContext(Dispatchers.Main) {

                shouldShowHideLoading.value = false
                onCreditCardSuccessful.value = true

                shouldEnableDisableUI.value = true
            }
        }
    }
}