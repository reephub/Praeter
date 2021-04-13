package com.praeter.ui.signup.successfulsignup;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.praeter.R;
import com.praeter.data.remote.dto.User;
import com.praeter.ui.base.BaseViewImpl;

import org.parceler.Parcels;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class SuccessfulSignUpView extends BaseViewImpl<SuccessfulSignUpPresenter>
        implements SuccessfulSignUpContract.View {

    private SuccessfulSignUpActivity context;

    // views
    @BindView(R.id.tv_title_congratulations)
    TextView tvTitleCongratulations;
    @BindView(R.id.tv_congratulations_msg)
    TextView tvCongratulationsMsg;
    @BindView(R.id.iv_animated_check)
    ImageView ivCheck;
    @BindView(R.id.btn_continue)
    Button btnContinue;

    AnimatedVectorDrawable mAnimationDrawable;

    @Inject
    SuccessfulSignUpView(SuccessfulSignUpActivity context) {
        this.context = context;
    }


    @Override
    public void onCreate() {

        getPresenter().attachView(this);

        ButterKnife.bind(this, context.findViewById(android.R.id.content));

        context.getSupportActionBar().setTitle(R.string.title_activity_successful_sign_up);

        Bundle extras = context.getIntent().getExtras();

        if (null == extras) {
            return;
        }

        User user = Parcels.unwrap(extras.getParcelable(SuccessfulSignUpActivity.EXTRA_USER));

        getPresenter().makeRestSaveCall(user);
    }

    @Override
    public void onDestroy() {
        getPresenter().detachView();
        if (null != context)
            context = null;
    }

    @Override
    public void onUserSaveSuccessful() {

        Observable observable1 =
                Observable
                        .create(emitter -> startAnimation(tvTitleCongratulations, emitter))
                        .doOnComplete(() -> Timber.d("observable 1 on complete"))
                        .doOnError(Timber::e);

        Observable observable2 =
                Observable
                        .create(emitter -> startAnimation(tvCongratulationsMsg, emitter))
                        .doOnComplete(() -> Timber.d("observable 2 on complete"))
                        .doOnError(Timber::e);

        Observable observable3 =
                Observable
                        .create(emitter -> startAnimation(ivCheck, emitter))
                        .doOnComplete(() -> Timber.d("observable 3 on complete"))
                        .doOnError(Timber::e);

        Observable observable4 =
                Observable
                        .create(this::animateButton)
                        .doOnComplete(() -> Timber.d("observable 4 on complete"))
                        .doOnError(Timber::e);

        Observable.concat(
                observable1,
                observable2,
                observable3,
                observable4
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    public void onUserSaveError(Throwable throwable) {
        Timber.e("onUserSaveError()");
        Timber.e(throwable);
    }


    @OnClick(R.id.btn_continue)
    void onContinueButtonClicked() {
        getPresenter().goToMainActivity();
    }


    public void startAnimation(final View view, final ObservableEmitter<Object> emitter) {
        Timber.d("startAnimation()");

        context.runOnUiThread(() -> {

            ObjectAnimator animator;
            PropertyValuesHolder pvhTranslate = PropertyValuesHolder.ofFloat("translationY", 50f, 0f);
            PropertyValuesHolder pvhAlpha = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);

            animator = ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslate, pvhAlpha);

            animator.setDuration(1000);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                    if (view == ivCheck) {
                        Drawable d = ivCheck.getDrawable();

                        if (d instanceof AnimatedVectorDrawable) {
                            mAnimationDrawable = (AnimatedVectorDrawable) d;
                            mAnimationDrawable.start();
                        }
                    }

                }

                @Override
                public void onAnimationEnd(Animator animation) {

                    view.setVisibility(View.VISIBLE);
                    emitter.onComplete();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();
        });
    }

    private void animateButton(final ObservableEmitter<Object> emitter) {
        Timber.d("animateButton()");

        context.runOnUiThread(() -> {
            btnContinue.animate()
                    .setDuration(1000)
                    .setInterpolator(new AccelerateInterpolator())
                    .alpha(1f)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            Timber.d("onAnimationStart()");

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            btnContinue.setVisibility(View.VISIBLE);
                            emitter.onComplete();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    })
                    .start();
        });
    }
}
