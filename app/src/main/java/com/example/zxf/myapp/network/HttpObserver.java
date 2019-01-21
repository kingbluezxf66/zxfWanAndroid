package com.example.zxf.myapp.network;

import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.zxf.myapp.model.BaseResponse;
import com.example.zxf.myapp.ui.login.LoginActivity;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

public abstract class HttpObserver<T> implements Observer<BaseResponse<T>> {
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    public HttpObserver(Context context, SwipeRefreshLayout swipeRefreshLayout) {
        this.context = context;
        this.swipeRefreshLayout =swipeRefreshLayout;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showShort("暂无网络，请检查网络连接");
            d.dispose();
            onHttpEnd();
            if(swipeRefreshLayout!=null){
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    @Override
    public void onNext(BaseResponse<T> t) {
        if(swipeRefreshLayout!=null){
            swipeRefreshLayout.setRefreshing(false);
        }
        if (t == null) {
            onFail(t);
        } else {
            int errorCode = t.getErrorCode();
            //未登录-1001，其他错误码为-1，成功为0，
            if(errorCode==0){
                onSuccess(t.getData());
            }else if(errorCode==-1001){
                onUnlogin();
            }else{
                onFail(t);
            }
        }

    }

    public void onUnlogin() {
       context.startActivity(new Intent(context,LoginActivity.class));
    }

    @Override
    public void onError(Throwable e) {
        onHttpError(e);
        onHttpEnd();
    }

    @Override
    public void onComplete() {
        onHttpComplete();
        onHttpEnd();

    }


    /**
     * 正常访问完成后调用(除非回调onHttpError方法)
     */
    public void onHttpComplete() {

    }

    /**
     * 无论错误或者成功都回调
     */
    public void onHttpEnd() {

    }

    /**
     * 数据解析失败 或者status返回的是0
     *
     * @param t
     */
    public void onFail(BaseResponse t) {
        if (t == null) {
            ToastUtils.showShort("数据异常");
        } else {
            String msg = t.getErrorMsg();
            if (!TextUtils.isEmpty(msg)) {
                ToastUtils.showShort(msg);
            } else {
                ToastUtils.showShort(t.getErrorCode());
            }
        }
    }

    /**
     * 访问 和解析数据成功 且返回status是1
     *
     * @param t baseBean中的info解析数据 可能为null
     */
    public abstract void onSuccess(T t);

    /**
     * 访问错误
     *
     * @param t 错误信息
     */
    public void onHttpError(Throwable t) {
        String msg = "未知错误";
        if (t instanceof UnknownHostException) {
            msg = "网络不可用";
        } else if (t instanceof ConnectException) {
            msg = "服务器连接失败";
        } else if (t instanceof SocketTimeoutException) {
            msg = "请求网络超时";
        } else if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            msg = convertStatusCode(httpException);
        } else if (t instanceof JsonParseException || t instanceof ParseException || t instanceof JSONException || t instanceof JsonIOException) {
            msg = "数据解析错误";
        }
        if (msg.length() != 0)
            ToastUtils.showShort(msg);
    }

    private String convertStatusCode(HttpException httpException) {
        String msg;
        if (httpException.code() == 500) {
            msg = "服务器发生错误";
        } else if (httpException.code() == 404) {
            msg = "请求地址不存在";
        } else if (httpException.code() == 403) {
            msg = "请求被服务器拒绝";
        } else if (httpException.code() == 307) {
            msg = "请求被重定向到其他页面";
        } else {
            msg = httpException.message();
        }
        return msg;
    }

}
