package com.praeter.ui.signup.userform;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSpinner;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
    // Confirm password
    @BindView(R.id.input_layout_confirm_password)
    TextInputLayout inputLayoutConfirmPassword;
    @BindView(R.id.input_confirm_password)
    TextInputEditText inputConfirmPassword;
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

        ButterKnife.bind(this, context.findViewById(android.R.id.content));

        context.getSupportActionBar().setTitle(
                context.getString(R.string.title_activity_user_inscription_form));

        setupSpinner();

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


    @OnClick(R.id.btn_continue)
    void onContinueButtonClicked() {
        Timber.d("onContinueButtonClicked()");

        Timber.d("check if field are correctly filled");
    }

    @Override
    public void onDestroy() {
        context = null;
    }
}
