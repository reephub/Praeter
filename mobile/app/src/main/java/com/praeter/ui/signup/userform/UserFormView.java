package com.praeter.ui.signup.userform;

import com.praeter.R;
import com.praeter.ui.base.BaseViewImpl;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class UserFormView extends BaseViewImpl<UserFormPresenter>
        implements UserFormContract.View {


    // TAG & Context
    private UserFormActivity context;

    @Inject
    UserFormView(UserFormActivity context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

        ButterKnife.bind(this, context.findViewById(android.R.id.content));

        context.getSupportActionBar().setTitle(
                context.getString(R.string.title_activity_user_inscription_form));

    }

    @Override
    public void onDestroy() {
        context = null;
    }
}
