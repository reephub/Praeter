package com.praeter.ui.signup.userform;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.praeter.BuildConfig;
import com.praeter.R;
import com.praeter.ui.base.BaseViewImpl;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class UserFormView extends BaseViewImpl<UserFormPresenter>
        implements UserFormContract.View {

    // TAG & Context
    private UserFormActivity context;

    //Views
    //Gender
    @BindView(R.id.sp_gender)
    AppCompatSpinner spGender;
    //Last Name
    @BindView(R.id.input_layout_last_name)
    TextInputLayout inputLayoutLastName;
    @BindView(R.id.input_last_name)
    TextInputEditText inputLastName;
    // First Name
    @BindView(R.id.input_layout_first_name)
    TextInputLayout inputLayoutFirstName;
    @BindView(R.id.input_first_name)
    TextInputEditText inputFirstName;
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
    // Confirm password
    @BindView(R.id.input_layout_confirm_password)
    TextInputLayout inputLayoutConfirmPassword;
    @BindView(R.id.input_confirm_password)
    TextInputEditText inputConfirmPassword;

    boolean isPasswordVisible = false;

    // Phone Number
    @BindView(R.id.input_layout_phone_number)
    TextInputLayout inputLayoutPhoneNumber;
    @BindView(R.id.input_phone_number)
    TextInputEditText inputPhoneNumber;
    // Date Of Birth
    @BindView(R.id.input_layout_date_of_birth)
    TextInputLayout inputLayoutDateOfBirth;
    @BindView(R.id.input_date_of_birth)
    TextInputEditText inputDateOfBirth;

    // Continue
    @BindView(R.id.btn_continue)
    Button continueButton;


    @Inject
    UserFormView(UserFormActivity context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

        getPresenter().attachView(this);

        ButterKnife.bind(this, context.findViewById(android.R.id.content));

        context.getSupportActionBar().setTitle(
                context.getString(R.string.title_activity_user_inscription_form));

        setupSpinner();

        if (BuildConfig.DEBUG)
            preloadData();

    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                context,
                android.R.layout.simple_spinner_item,
                Arrays.asList(context.getResources().getStringArray(R.array.user_form_gender))) {

            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.WHITE);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(adapter);
    }

    private void preloadData() {
        inputLastName.setText("Doe");
        inputFirstName.setText("John");
        inputEmail.setText("john.doe@test.fr");
        inputPassword.setText("johndoe");
        inputConfirmPassword.setText("johndoe");
        inputPhoneNumber.setText("06123456789");
    }


    @OnClick(R.id.btn_continue)
    void onContinueButtonClicked() {
        Timber.d("onContinueButtonClicked()");

        // TODO : check if field are correctly filled
        Timber.d("check if field are correctly filled");

        getPresenter().goToPlanActivity();
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

    @Override
    public void onDestroy() {
        context = null;
    }
}
