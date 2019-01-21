package com.example.zxf.myapp.ui.regist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.blankj.utilcode.util.RegexUtils;
import com.example.zxf.myapp.helper.RetrofitHelper;
import com.example.zxf.myapp.model.RegistResponse;
import com.example.zxf.myapp.network.HttpObservable;
import com.example.zxf.myapp.network.HttpObserver;

import java.util.HashMap;
import java.util.Map;

public class RegistPresenter extends RegistContract.Presenter {
    private Context context;

    @NonNull
    public static RegistPresenter newInstance() {
        return new RegistPresenter();
    }

    @Override
    public void goRegist(String username, String password, String confirmPassword) {
        if (mIView == null)
            return;
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            mIView.showToast("请输入完整信息");
            return;
        }
        if (!RegexUtils.isMobileExact(username)) {
            mIView.showToast("请输入正确手机号");
            return;
        }
//        if (!RegexUtils.isMatch("^(?![a-z0-9]+$)(?![A-Za-z]+$)(?![A-Z0-9]+$)[a-zA-Z0-9]{8,20}$", password)) {
//            ToastUtils.showShort("请输入8-20位大小写字母、数字组合密码");
//            return;
//        }
        if (!password.equals(confirmPassword)) {
            mIView.showToast("两次密码输入不一致");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("repassword", confirmPassword);
        HttpObservable.getObservable(RetrofitHelper.dataManager.getRegistMessage(map)).subscribe(new HttpObserver<RegistResponse>(context, null) {
            @Override
            public void onSuccess(RegistResponse registResponse) {
                mIView.showRegistSuccess(registResponse);
            }
        });
    }
}
