package com.praeter.ui.signup.userform;

import com.praeter.data.remote.dto.User;
import com.praeter.ui.base.BaseView;

public interface UserFormContract {

    interface View extends BaseView {
    }

    interface Presenter {
        void goToPlanActivity(User user);
    }
}
