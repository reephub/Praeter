package com.praeter.ui.signup.successfulsignup;

import com.praeter.data.remote.dto.User;
import com.praeter.ui.base.BaseView;

public interface SuccessfulSignUpContract {

    interface View extends BaseView {
        void onUserSaveSuccessful();

        void onUserSaveError(Throwable throwable);
    }

    interface Presenter {
        void makeRestSaveCall(User user);

        void goToMainActivity();
    }
}
