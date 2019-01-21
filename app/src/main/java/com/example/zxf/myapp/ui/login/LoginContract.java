package com.example.zxf.myapp.ui.login;

import com.example.zxf.myapp.base.BasePresenter;
import com.example.zxf.myapp.base.BaseView;

public interface LoginContract {
    abstract class Presenter extends BasePresenter<LoginView> {
       public abstract void goLogin(String username,String password);
       public abstract void goRegist();
    }
    interface LoginView extends BaseView {
        String getUsername();

        String getPassword();

        void loginSuccess();

    }
}
