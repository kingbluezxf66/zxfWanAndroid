package com.example.zxf.myapp.network;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HttpObservable<T> {
    public static <T> Observable<T> getObservable(Observable<T> observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(2, TimeUnit.SECONDS)
                .unsubscribeOn(Schedulers.io());
    }
}
