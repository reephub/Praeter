package com.praeter.ui.splashscreen

import android.Manifest
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnticipateInterpolator
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.praeter.databinding.ActivitySplashscreenBinding
import com.praeter.navigator.Navigator
import com.praeter.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SplashScreenActivity : BaseActivity() {

    private var _viewBinding: ActivitySplashscreenBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    private val mViewModel: SplashScreenViewModel by viewModels()

    private var splashScreen: SplashScreen? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Handle the splash screen transition.
            splashScreen = installSplashScreen()
        }

        _viewBinding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            /*
            // Get the duration of the animated vector drawable.
            val animationDuration = splashScreenView.iconAnimationDurationMillis
            // Get the start time of the animation.
            val animationStart = splashScreenView.iconAnimationDurationMillis
            // Calculate the remaining duration of the animation.
            val remainingDuration = (
                    animationDuration - (SystemClock.uptimeMillis() - animationStart)
                    ).coerceAtLeast(0L)
            */
            // Set up an OnPreDrawListener to the root view.
            val content: View = findViewById(android.R.id.content)
            content.viewTreeObserver.addOnPreDrawListener(
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        // Check if the initial data is ready.
                        return if (mViewModel.isReady) {
                            // The content is ready; start drawing.
                            content.viewTreeObserver.removeOnPreDrawListener(this)
                            true
                        } else {
                            // The content is not ready; suspend.
                            false
                        }
                    }
                }
            )

            // Add a callback that's called when the splash screen is animating to
            // the app content.
            splashScreen?.setOnExitAnimationListener { splashScreenView ->
                // Create your custom animation.
                val slideUp = ObjectAnimator.ofFloat(
                    splashScreenView,
                    "translationY",
                    0f,
                    -splashScreenView.view.height.toFloat()
                )
                slideUp.interpolator = AnticipateInterpolator()
                slideUp.duration = 200L

                // Call SplashScreenView.remove at the end of your custom animation.
                slideUp.doOnEnd { splashScreenView.remove() }

                // Run your animation.
                slideUp.start()
            }
        }

        checkPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    private fun checkPermissions() {
        Dexter
            .withContext(this)
            .withPermissions(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        // do you work now
                        Timber.d("All permissions are granted!")
                        onPermissionsGranted()
                    }

                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) {
                        // permission is denied permanently, navigate user to app settings
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .withErrorListener { error: DexterError ->
                Toast.makeText(
                    this@SplashScreenActivity,
                    "Error occurred! $error",
                    Toast.LENGTH_SHORT
                ).show()
                onPermissionsDenied()
            }
            .onSameThread()
            .check()
    }

    private fun onPermissionsGranted() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)

            goToServicePickerActivity()
        }
    }

    private fun onPermissionsDenied() {
        Timber.e("onPermissionsDenied()")
        closeApp()
    }

    private fun closeApp() {
        finish()
    }

    /////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////
    private fun goToServicePickerActivity() {
        Timber.i("goToServicePickerActivity()")

        //navigator.callServicePickerActivity();
        Navigator(this).callMainActivity()
        finish()
    }
}