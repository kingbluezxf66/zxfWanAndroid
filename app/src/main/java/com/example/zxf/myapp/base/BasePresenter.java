package com.example.zxf.myapp.base;

import android.support.annotation.NonNull;


public abstract class BasePresenter<V> {
    public V mIView;


    /**
     * 绑定View的引用
     * @param v view
     */
    public void attachView( @NonNull V v) {
        this.mIView = v;
    }

    /**
     * 解绑View
     */
    public void detachView() {
        mIView = null;
    }
}
