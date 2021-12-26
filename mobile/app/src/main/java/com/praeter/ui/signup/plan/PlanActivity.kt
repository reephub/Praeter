package com.praeter.ui.signup.plan

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import com.praeter.data.remote.dto.user.User
import com.praeter.databinding.ActivityPlanBinding
import com.praeter.navigator.Navigator
import com.praeter.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlanActivity : BaseActivity() {

    private var _viewBinding: ActivityPlanBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    var rbFreePlan: RadioButton? = null
    var rbPremiumPlan: RadioButton? = null
    var btnContinue: Button? = null

    private var user: User? = null

   val navigator: Navigator = Navigator(this@PlanActivity)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _viewBinding = ActivityPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras: Bundle = intent.extras ?: return
        user = extras.getParcelable(EXTRA_USER)
    }

    fun continueClick(view: View) {

        if (rbFreePlan!!.isChecked) {
            user?.isPremium = false
            navigator.callSuccessfulSignUpActivity(user)
        }
        if (rbPremiumPlan!!.isChecked) {
            user?.isPremium = true
            navigator.callPremiumPlanActivity(user)
        }
        finish()
    }

    companion object {
        const val EXTRA_USER = "EXTRA_USER"
    }
}