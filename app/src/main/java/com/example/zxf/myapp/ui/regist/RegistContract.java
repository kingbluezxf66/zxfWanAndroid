package com.example.zxf.myapp.ui.regist;

import com.example.zxf.myapp.base.BasePresenter;
import com.example.zxf.myapp.base.BaseView;
import com.example.zxf.myapp.model.RegistResponse;

/**
 * 契约接口，用来存放相同业务的Presenter和view接口
 */
public interface RegistContract {
    abstract class Presenter extends BasePresenter<RegistView> {
        protected abstract void goRegist(String username, String password, String confirmPassword);
    }
    interface RegistView extends BaseView{
        String getUserName();
        String getPassword();
        String getConfirmPassword();
        void showRegistSuccess(RegistResponse registResponse);
    }
}
