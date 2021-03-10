package com.praeter.ui.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.praeter.R;
import com.praeter.data.remote.dto.User;
import com.praeter.navigator.Navigator;
import com.praeter.ui.base.BaseViewImpl;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class LoginView extends BaseViewImpl<LoginPresenter>
        implements LoginActivityContract.View {

    // TAG & Context
    private LoginActivity context;

    //Views
    //Email
    @BindView(R.id.input_layout_email)
    TextInputLayout inputLayoutEmail;
    @BindView(R.id.input_email)
    TextInputEditText inputEmail;
    // Password
    @BindView(R.id.input_layout_password)
    TextInputLayout inputLayoutPassword;
    @BindView(R.id.input_password)
    TextInputEditText inputPassword;

    @BindView(R.id.btn_password_visibility)
    ImageButton btnPasswordVisibility;

    @BindView(R.id.btn_no_account_register)
    Button tvNoAccount;

    @BindView(R.id.btn_enter)
    Button btnEnter;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    boolean isPasswordVisible = false;

    @Inject
    Navigator navigator;

    @Inject
    LoginView(LoginActivity context) {
        this.context = context;
    }


    @Override
    public void onCreate() {

        getPresenter().attachView(this);

        ButterKnife.bind(this, context.findViewById(android.R.id.content));

//        setSupportActionBar(toolbar);
        context.getSupportActionBar().setTitle(
                context.getString(R.string.title_activity_login));

        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));

        //https://stackoverflow.com/questions/17989733/move-to-another-edittext-when-soft-keyboard-next-is-clicked-on-android
        inputPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard(context, context.findViewById(android.R.id.content));
                    return true;
                }
                return false;
            }
        });

        preloadData();

    }


    void preloadData() {
        inputEmail.setText("jane.mills@test.fr");
        inputPassword.setText("jm_test");
    }

    @Override
    public void showLoading() {
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        context.runOnUiThread(() -> {
            if (progressBar != null)
                progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    public void onLoginSuccessful() {
        if (context != null && navigator != null) {
            navigator.callMainActivity();
            context.finish();
        }
    }

    @Override
    public void onLoginFailed() {

    }

    @Override
    public void closeApp() {
        context.finish();
    }

    @Override
    public void onDestroy() {
        getPresenter().detachView();
        context = null;
    }

    @OnClick(R.id.btn_password_visibility)
    void onPasswordVisibilityButtonClicked() {
        Timber.d("onPasswordVisibilityButtonClicked()");

        // If flag is false - password hidden (default)
        if (!isPasswordVisible) {
            inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            btnPasswordVisibility.setImageDrawable(context.getDrawable(R.drawable.ic_visibility_off));
            // Tint color programmatically
            // https://stackoverflow.com/questions/20121938/how-to-set-tint-for-an-image-view-programmatically-in-android
            btnPasswordVisibility.setColorFilter(ContextCompat.getColor(context, R.color.purple_200), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            btnPasswordVisibility.setImageDrawable(context.getDrawable(R.drawable.ic_visibility));
            btnPasswordVisibility.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
        }

        isPasswordVisible = !isPasswordVisible;
    }


    @OnClick(R.id.btn_enter)
    void onButtonClicked() {
        login();
    }

    @OnClick(R.id.btn_no_account_register)
    void onNoAccountClicked() {
        navigator.callSignUpActivity();
    }

    /**
     * logging in user. Will make http post request with name, email
     * as parameters
     */
    private void login() {

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        hideKeyboard(context, context.findViewById(android.R.id.content));

        Timber.e("login()");

        final String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();

        Timber.d("make rest call login");

        getPresenter().makeCallLogin(new User(email, password));
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            context
                    .getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    // Validating email
    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(context.getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }


    // Validating password
    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(context.getString(R.string.err_msg_name));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }


    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email)
                && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    /**
     * Hide the keyboard
     *
     * @param view
     */
    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_password:
                    validatePassword();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
            }
        }
    }
}