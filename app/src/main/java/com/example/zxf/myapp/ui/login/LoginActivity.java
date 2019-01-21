package com.example.zxf.myapp.ui.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zxf.myapp.AppActivitManager;
import com.example.zxf.myapp.MyApplication;
import com.example.zxf.myapp.R;
import com.example.zxf.myapp.base.BaseMVPActivity;
import com.example.zxf.myapp.base.BasePresenter;
import com.example.zxf.myapp.helper.DataBaseHelper;
import com.example.zxf.myapp.ui.activity.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseMVPActivity<LoginContract.Presenter> implements LoginContract.LoginView {

    @BindView(R.id.tie_phone)
    TextInputEditText tiePhone;
    @BindView(R.id.tie_pass)
    TextInputEditText tiePass;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.tv_regist)
    TextView tvRegist;
    private int REGISTCODE = 1000;
    private int LOGINCODE = 2;
    @Inject
    DataBaseHelper dataBaseHelper;

    @Override
    protected int getLayout() {
        return R.layout.activty_login;
    }

    @Override
    protected void initView() {
        MyApplication.get(this).getDatabaseHelperComponent().inject(this);
    }

    @OnClick({R.id.bt_login, R.id.tv_regist})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                mPresenter.goLogin(getUsername(), getPassword());
                break;
            case R.id.tv_regist:
                mPresenter.goLogin(getUsername(), getPassword());
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGISTCODE && resultCode == LOGINCODE) {
            String userName = data.getStringExtra("userName");
            tiePhone.setText(userName);
        }
    }

    @NonNull
    @Override
    public BasePresenter initPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public String getUsername() {
        return tiePhone.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return tiePass.getText().toString();
    }

    @Override
    public void loginSuccess() {
        showToast("登录成功");
        dataBaseHelper.saveUserInfo(getUsername(), getPassword(), true);
        AppActivitManager.getAppActivityManager().finishActivity(this);
        startActivity(MainActivity.class);
    }
}
