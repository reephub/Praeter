package com.praeter.ui.servicepicker

import android.os.Bundle
import android.view.View
import com.praeter.databinding.ActivityServicePickerBinding
import com.praeter.navigator.Navigator
import com.praeter.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ServicePickerActivity : BaseActivity() {

    private var _viewBinding: ActivityServicePickerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    val navigator: Navigator = Navigator(this@ServicePickerActivity)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityServicePickerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("onDestroy()")
        _viewBinding = null
    }

    fun searchService(view: View) {
        Timber.e("searchService()")
        navigator.callLoginActivity("login_normal")
    }

    fun provideService(view: View) {
        Timber.e("provideService()")
        navigator.callLoginActivity("login_provider")
    }
}