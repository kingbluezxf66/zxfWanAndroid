package com.example.zxf.myapp.rxbus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


/**
 * 项目名称：MyApplication
 * 类描述：RxJava实现RxBus事件总线
 * 创建人：zxf
 * 创建时间：2018/12/20 15:13
 */

public class RxBus {
    private static volatile RxBus rxBus;

    private RxBus() {
    }

    //Observable 和 observer 之间的桥梁
    private Subject subject = PublishSubject.create().toSerialized();

    public static RxBus getInstance() {
        //使用dcl双重检查模式创建RxBus对象
        if (rxBus == null) {
            synchronized (RxBus.class) {
                if (rxBus == null) {
                    rxBus = new RxBus();
                }
            }
        }
        return rxBus;
    }

    public void post(Object o) {
        subject.onNext(o);
    }

    public <T> Observable<T> tObservable(Class<T> eventType) {
        return subject.ofType(eventType);
    }
}
