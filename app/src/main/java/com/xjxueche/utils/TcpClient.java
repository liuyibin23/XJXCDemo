package com.xjxueche.utils;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import kotlin.jvm.Throws;

/**
 * Created by 刘乙镔 on 2017/7/26.
 */

public class TcpClient {

    static ObservableOnSubscribe<String> subscribe;

//    public static void Start() {
//        Thread recevieThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
////                subscribe = new ObservableOnSubscribe<String>() {
////                    @Override
////                    public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
////                        for (int i = 0; i < 50 ;i++){
////                            e.onNext(Integer.toString(i));
////                            System.out.println("recevied:"+Integer.toString(i) + " on " + Thread.currentThread());
////                            Thread.sleep(200);
////                        }
////                        e.onComplete();
////                    }
////                };
//
//                for (int i = 0; i < 50 ;i++){
//
//                }
//
//            }
//        });
//        recevieThread.start();
//    }

    public static Observable<String> tcpData() throws Exception {
//        Observable<String> tcpObservable = Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
//                for (int i = 0; i < 50 ;i++){
//                    e.onNext(Integer.toString(i));
//                    System.out.println("recevied:"+Integer.toString(i) + " on " + Thread.currentThread());
//                    Thread.sleep(200);
//                }
//                e.onComplete();
//            }
//        });
//        if(subscribe == null)
//        {
//            throw new Exception("tcp连接未打开");
//        }
//        Observable<String> tcpObservable = Observable.create(subscribe);


        Observable<String> tcpObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
//                for (int i = 0; i < 50 ;i++){
//                    e.onNext(Integer.toString(i));
//                    System.out.println("recevied:"+Integer.toString(i) + " on " + Thread.currentThread());
//                    Thread.sleep(200);
//                }
                final ObservableEmitter<String> emitter = e;

                //接收数据这不需要新开线程，由observable的subscribeOn来指定线程
                for (int i = 0; i < 50; i++) {
                    emitter.onNext(Integer.toString(i));
                    System.out.println("recevied:" + Integer.toString(i) + " on " + Thread.currentThread());
                    if(i == 10)
                    {
                        throw new Exception("接收异常");
                    }
                    Thread.sleep(200);
                }
                e.onComplete();
            }
        });

        return tcpObservable;
    }



}
