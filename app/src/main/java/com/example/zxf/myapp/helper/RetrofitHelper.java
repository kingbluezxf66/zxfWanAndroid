package com.example.zxf.myapp.helper;

import android.util.Log;

import com.example.zxf.myapp.MyApplication;
import com.example.zxf.myapp.network.AddCookiesInterceptor;
import com.example.zxf.myapp.network.DataManager;
import com.example.zxf.myapp.network.GsonConverterFactory;
import com.example.zxf.myapp.network.ReceivedCookiesInterceptor;
import com.example.zxf.myapp.network.RequestInterface;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RetrofitHelper {
    public static DataManager dataManager = new DataManager(getRequestInterface());
    private static RequestInterface getRequestInterface() {
        /*
         **打印retrofit信息部分
         */
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                //打印retrofit日志
                Log.e("RetrofitLog", "retrofitBack = " + message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //okhttp设置部分，此处还可再设置网络参数
        OkHttpClient client = new OkHttpClient.Builder()//okhttp设置部分，此处还可再设置网络参数
                .connectTimeout(30, TimeUnit.SECONDS)
                .callTimeout(30, TimeUnit.SECONDS)
                .addNetworkInterceptor(loggingInterceptor)
                .addInterceptor(new AddCookiesInterceptor(MyApplication.context)) //持久化cookie
                .addInterceptor(new ReceivedCookiesInterceptor(MyApplication.context))
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RequestInterface.baseUrl)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        return requestInterface;
    }
}
