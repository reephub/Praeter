package com.praeter.ui.signup.userform;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.praeter.BuildConfig;
import com.praeter.R;
import com.praeter.data.remote.dto.User;
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

    private User user;
    private String szGender = "";

    @Inject
    UserFormView(UserFormActivity context) {
        this.context = context;
    }


    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    @Override
    public void onCreate() {

        getPresenter().attachView(this);

        ButterKnife.bind(this, context.findViewById(android.R.id.content));

        context.getSupportActionBar().setTitle(context.getString(R.string.title_activity_user_inscription_form));
        context.getSupportActionBar().setTitle(context.getString(R.string.title_activity_user_inscription_form));

        user = new User();

        setupSpinner();

        if (BuildConfig.DEBUG)
            preloadData();
    }


    /////////////////////////////////////
    //
    // BUTTERKNIFE
    //
    /////////////////////////////////////
    @OnClick(R.id.btn_continue)
    void onContinueButtonClicked() {
        Timber.d("onContinueButtonClicked()");

        Timber.d("check if field are correctly filled");
        submitForm();
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


    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////
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
        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Timber.d("Position : %s", position);

                switch (position) {
                    case 1:
                        szGender = "male";
                        break;
                    case 2:
                        szGender = "female";
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Timber.e("Nothing selected");
            }
        });
    }

    private void preloadData() {
        inputLastName.setText("Doe");
        inputFirstName.setText("John");
        inputEmail.setText("john.doe@test.fr");
        inputPassword.setText("johndoe");
        inputConfirmPassword.setText("johndoe");
        inputPhoneNumber.setText("06123456789");
    }

    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateLastName()) {
            return;
        }
        if (!validateFirstName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
        if (!validateConfirmPassword()) {
            return;
        }

        if (!validatePhone()) {
            return;
        }

        if (!validateDateOfBirth()) {
            return;
        }

        user = new User(
                szGender,
                inputFirstName.getText().toString(),
                inputLastName.getText().toString(),
                inputEmail.getText().toString(),
                inputPassword.getText().toString(),
                inputPhoneNumber.getText().toString(),
                inputDateOfBirth.getText().toString()
        );

        Toast.makeText(context, "Thank You!", Toast.LENGTH_SHORT).show();

        getPresenter().goToPlanActivity(user);
    }

    private boolean validateLastName() {
        if (inputLastName.getText().toString().trim().isEmpty()) {
            inputLayoutLastName.setError(context.getString(R.string.err_msg_form_last_name));
            requestFocus(inputLastName);
            return false;
        } else {
            inputLayoutLastName.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateFirstName() {
        if (inputFirstName.getText().toString().trim().isEmpty()) {
            inputLayoutFirstName.setError(context.getString(R.string.err_msg_form_first_name));
            requestFocus(inputFirstName);
            return false;
        } else {
            inputLayoutFirstName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(context.getString(R.string.err_msg_form_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(context.getString(R.string.err_msg_form_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateConfirmPassword() {
        if (inputConfirmPassword.getText().toString().trim().isEmpty()
                && !inputConfirmPassword.getText().toString().equals(inputPassword.getText().toString())) {
            inputLayoutConfirmPassword.setError(context.getString(R.string.err_msg_form_confirm_password));
            requestFocus(inputConfirmPassword);
            return false;
        } else {
            inputLayoutConfirmPassword.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validatePhone() {
        if (inputPhoneNumber.getText().toString().trim().isEmpty()) {
            inputLayoutPhoneNumber.setError(context.getString(R.string.err_msg_form_phone_number));
            requestFocus(inputPhoneNumber);
            return false;
        } else {
            inputLayoutPhoneNumber.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateDateOfBirth() {
        /*if (inputDateOfBirth.getText().toString().trim().isEmpty()) {
            inputLayoutDateOfBirth.setError(context.getString(R.string.err_msg_form_date_of_birth));
            requestFocus(inputDateOfBirth);
            return false;
        } else {
            inputLayoutDateOfBirth.setErrorEnabled(false);
        }
*/
        return true;
    }


    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            context.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    @Override
    public void onDestroy() {
        getPresenter().detachView();
        context = null;
    }
}
