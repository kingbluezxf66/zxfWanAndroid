package com.example.zxf.myapp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.zxf.myapp.CircleProgress;
import com.example.zxf.myapp.MyApplication;
import com.example.zxf.myapp.R;
import com.example.zxf.myapp.databinding.ActivitySplashBinding;
import com.example.zxf.myapp.helper.DataBaseHelper;
import com.example.zxf.myapp.model.SplashBean;
import com.example.zxf.myapp.model.User;
import com.example.zxf.myapp.ui.login.LoginActivity;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class SplashActivity extends AppCompatActivity {

//    @BindView(R.id.circleProgress)
    private CircleProgress circleProgress;
    private Disposable disposable;
    private boolean isClick;
    @Inject
    DataBaseHelper dataBaseHelper;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplashBinding basicBinding;
        basicBinding = DataBindingUtil.setContentView(this,R.layout.activity_splash);
        SplashBean splashBean = new SplashBean("跳转");
        basicBinding.setSplash(splashBean);
//        setContentView(R.layout.activity_splash);
//        ButterKnife.bind(this);
        circleProgress = basicBinding.circleProgress;
        MyApplication.get(this).getDatabaseHelperComponent().inject(this);
        circleProgress.setOutLineColor(Color.TRANSPARENT);
        circleProgress.setOutLineWidth(6);
        circleProgress.setInCircleColor(R.color.color_99);
        circleProgress.setProgressColor(R.color.colorPrimary);
        circleProgress.setProgressType(CircleProgress.ProgressType.COUNT_BACK);
        circleProgress.setTimeMillis(3000);
        circleProgress.reStart();
        //使用rxjava 实现倒计时功能
        disposable = Flowable.intervalRange(0, 3, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        circleProgress.setText((3 - aLong) + "s");
                    }
                }).doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (!isClick) {
                            openAnotherActivity();
                        }
                    }
                })
                .subscribe();
    }

    @OnClick(R.id.circleProgress)
    public void turnAnother() {
        isClick = true;
        openAnotherActivity();
    }

    private void openAnotherActivity() {
        User user = dataBaseHelper.getUserInfo();
        if (user == null) {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
