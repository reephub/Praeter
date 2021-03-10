package com.praeter.ui.signup.successfulsignup;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.praeter.R;
import com.praeter.data.remote.dto.User;
import com.praeter.ui.base.BaseViewImpl;

import org.parceler.Parcels;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class SuccessfulSignUpView extends BaseViewImpl<SuccessfulSignUpPresenter>
        implements SuccessfulSignUpContract.View, Animation.AnimationListener {

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

    Animation animFadeIn;
    Animation animSlideUp;

    boolean hasMsgAnimationStarted = false;

    @Inject
    SuccessfulSignUpView(SuccessfulSignUpActivity context) {
        this.context = context;
    }


    @Override
    public void onCreate() {

        getPresenter().attachView(this);

        ButterKnife.bind(context);

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
        startAnimation(tvTitleCongratulations);

        Completable
                .complete()
                .delay(500, TimeUnit.MILLISECONDS)
                .doOnComplete(() -> startAnimation(tvCongratulationsMsg))
                .doOnError(Timber::e)
                .doAfterTerminate(() -> startAnimation(ivCheck))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    public void onUserSaveError(Throwable throwable) {
        Timber.e(throwable);
    }


    @OnClick(R.id.btn_continue)
    void onContinueButtonClicked() {
        getPresenter().goToMainActivity();
    }


    public void startAnimation(final View view) {
        Timber.d("startAnimation()");

        animFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        animFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Timber.d("onAnimationStart()");

                view.setVisibility(View.VISIBLE);

                if (view == ivCheck)
                    hasMsgAnimationStarted = true;

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Timber.d("onAnimationEnd()");

                if (view == ivCheck) {
                    Drawable d = ivCheck.getDrawable();

                    if (d instanceof AnimatedVectorDrawable) {
                        mAnimationDrawable = (AnimatedVectorDrawable) d;
                        mAnimationDrawable.start();
                    }
                }


                if (hasMsgAnimationStarted) {

                    Completable
                            .complete()
                            .delay(500, TimeUnit.MILLISECONDS)
                            .doOnComplete(() -> animateButton())
                            .doOnError(Timber::e)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Timber.d("onAnimationRepeat()");
            }
        });

        animSlideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);

        final AnimationSet mAnimationSet = new AnimationSet(true);

        mAnimationSet.setInterpolator(new AccelerateInterpolator());

        mAnimationSet.addAnimation(animFadeIn);
        mAnimationSet.addAnimation(animSlideUp);

        view.startAnimation(mAnimationSet);
    }

    private void animateButton() {
        Timber.d("animateButton()");
        animFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        animFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Timber.d("onAnimationStart()");
                btnContinue.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Timber.d("onAnimationEnd()");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Timber.d("onAnimationRepeat()");
            }
        });

        final AnimationSet mAnimationSet = new AnimationSet(true);

        mAnimationSet.setInterpolator(new AccelerateInterpolator());

        mAnimationSet.addAnimation(animFadeIn);
        btnContinue.startAnimation(mAnimationSet);
    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
