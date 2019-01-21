package com.example.zxf.myapp;

import android.app.Application;
import android.content.Context;

import com.example.zxf.myapp.dagger.DaggerDataBaseHelperComponent;
import com.example.zxf.myapp.dagger.DataBaseHelperComponent;
import com.tencent.bugly.crashreport.CrashReport;

import io.realm.Realm;

public class MyApplication extends Application {
    public static Context context;
    private DataBaseHelperComponent dataBaseHelperComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        context = this;
        dataBaseHelperComponent = DaggerDataBaseHelperComponent.builder().build();
        CrashReport.initCrashReport(this, "51583aabb6", true);

    }

    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

    public DataBaseHelperComponent getDatabaseHelperComponent() {
        return dataBaseHelperComponent;
    }
}
