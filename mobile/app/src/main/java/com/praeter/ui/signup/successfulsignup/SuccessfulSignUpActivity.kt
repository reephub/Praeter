package com.praeter.ui.signup.successfulsignup

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import androidx.activity.viewModels
import com.praeter.data.remote.dto.user.User
import com.praeter.databinding.ActivitySuccessfulSignUpBinding
import com.praeter.navigator.Navigator
import com.praeter.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber

@AndroidEntryPoint
class SuccessfulSignUpActivity : BaseActivity() {

    private var _viewBinding: ActivitySuccessfulSignUpBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    private val mViewModel: SuccessfulSignUpViewModel by viewModels()

    var mAnimationDrawable: AnimatedVectorDrawable? = null

    val navigator: Navigator = Navigator(this@SuccessfulSignUpActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivitySuccessfulSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras: Bundle = intent.extras ?: return
        val user: User = extras.getParcelable(EXTRA_USER) ?: return

        initViewModelObservers()

        CoroutineScope(Dispatchers.IO).launch {
            mViewModel.makeRestSaveCall(user)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("onDestroy()")
        _viewBinding = null
    }

    fun continueClick(view: View) {
        goToMainActivity()
    }

    private fun initViewModelObservers() {
        mViewModel.getOnUserSaveSuccessful().observe(this, {
            val job1: Job = CoroutineScope(Dispatchers.Main).launch {
                startAnimation(binding.tvTitleCongratulations)
            }
            val job2: Job = CoroutineScope(Dispatchers.Main).launch {
                startAnimation(binding.tvCongratulationsMsg)
            }
            val job3: Job = CoroutineScope(Dispatchers.Main).launch {
                startAnimation(binding.ivAnimatedCheck)
            }
            val job4: Job = CoroutineScope(Dispatchers.Main).launch {
                animateButton()
            }

            CoroutineScope(Dispatchers.Main).launch {
                joinAll(job1, job2, job3, job4)
            }
        })

        mViewModel.getOnUserSaveError().observe(this, {
            Timber.e("onUserSaveError()")
        })
    }

    private fun goToMainActivity() {
        navigator.callMainActivity()
    }

    fun startAnimation(view: View) {
        Timber.d("startAnimation()")

        val animator: ObjectAnimator
        val pvhTranslate: PropertyValuesHolder =
            PropertyValuesHolder.ofFloat("translationY", 50f, 0f)
        val pvhAlpha: PropertyValuesHolder = PropertyValuesHolder.ofFloat("alpha", 0f, 1f)
        animator = ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslate, pvhAlpha)
        animator.duration = 1000
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                if (view === binding.ivAnimatedCheck) {
                    val d: Drawable = binding.ivAnimatedCheck!!.drawable
                    if (d is AnimatedVectorDrawable) {
                        mAnimationDrawable = d
                        mAnimationDrawable!!.start()
                    }
                }
            }

            override fun onAnimationEnd(animation: Animator) {
                view!!.visibility = View.VISIBLE
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        animator.start()

    }

    private fun animateButton() {
        Timber.d("animateButton()")
        binding.btnContinue.animate()
            .setDuration(1000)
            .setInterpolator(AccelerateInterpolator())
            .alpha(1f)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    Timber.d("onAnimationStart()")
                }

                override fun onAnimationEnd(animation: Animator) {
                    binding.btnContinue.visibility = View.VISIBLE

                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            .start()
    }

    companion object {
        const val EXTRA_USER = "EXTRA_USER"
    }
}