package com.example.zxf.myapp.ui.regist;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.widget.Button;

import com.blankj.utilcode.util.ToastUtils;
import com.example.zxf.myapp.R;
import com.example.zxf.myapp.base.BaseMVPActivity;
import com.example.zxf.myapp.base.BasePresenter;
import com.example.zxf.myapp.model.RegistResponse;

import butterknife.BindView;
import butterknife.OnClick;

public class RegistActivity extends BaseMVPActivity<RegistContract.Presenter> implements RegistContract.RegistView{

    @BindView(R.id.tie_phone)
    TextInputEditText tiePhone;
    @BindView(R.id.tie_pass)
    TextInputEditText tiePass;
    @BindView(R.id.tie_con_pass)
    TextInputEditText tieConPass;
    @BindView(R.id.bt_regist)
    Button btRegist;
    private int LOGINCODE = 2;

    @Override
    protected int getLayout() {
        return R.layout.activity_regist;
    }

    @Override
    protected void initView(){ }

    @OnClick(R.id.bt_regist)
    protected void goRegist(){
        mPresenter.goRegist(getUserName(),getPassword(),getConfirmPassword());
    }

    @Override
    public String getUserName() {
        return tiePhone.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return tiePass.getText().toString();
    }

    @Override
    public String getConfirmPassword() {
        return tieConPass.getText().toString();
    }

    @Override
    public void showRegistSuccess(RegistResponse registResponse) {
        ToastUtils.showShort("注册成功");
                if(registResponse!=null&&!TextUtils.isEmpty(registResponse.getUsername())){
                    Intent intent = new Intent();
                    intent.putExtra("userName", registResponse.getUsername());
                    setResult(LOGINCODE, intent);
                }
                finish();
    }

    @NonNull
    @Override
    public BasePresenter initPresenter() {
        return RegistPresenter.newInstance();
    }
}
