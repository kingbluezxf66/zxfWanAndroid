package com.example.zxf.myapp.ui.login;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.RegexUtils;
import com.example.zxf.myapp.helper.RetrofitHelper;
import com.example.zxf.myapp.model.RegistResponse;
import com.example.zxf.myapp.network.HttpObservable;
import com.example.zxf.myapp.network.HttpObserver;
import com.example.zxf.myapp.ui.regist.RegistActivity;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;


public class LoginPresenter extends LoginContract.Presenter {
    private Context context;
    private int REGISTCODE = 1000;
    @Nonnull
    public LoginPresenter( Context context) {
        this.context = context;
    }


//    public static LoginPresenter getNewInstance(DataBaseHelper dataBaseHelper, Context context) {
//        return new LoginPresenter(dataBaseHelper, context);
//    }

    @Override
    public void goLogin(String username, String password) {
        if (mIView == null)
            return;
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            mIView.showToast("用户名或者密码不能为空");
            return;
        }
        if (!RegexUtils.isMobileExact(username)) {
            mIView.showToast("请输入正确手机号");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        HttpObservable.getObservable(RetrofitHelper.dataManager.goLoginMessage(map)).subscribe(new HttpObserver<RegistResponse>(context, null) {
            @Override
            public void onSuccess(RegistResponse registResponse) {
                if (registResponse != null && !TextUtils.isEmpty(registResponse.getUsername())) {
                    mIView.loginSuccess();

                }

            }
        });
    }

    @Override
    public void goRegist() {
        mIView.startNewActivityForResult(RegistActivity.class,null,REGISTCODE);
    }
}
