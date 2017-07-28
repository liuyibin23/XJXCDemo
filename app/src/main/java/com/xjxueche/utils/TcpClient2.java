package com.xjxueche.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 刘乙镔 on 2017/7/26.
 */

public class TcpClient2 {

    private ObservableOnSubscribe<String> subscribe;
    private Socket socket;
    private boolean isConnected = true;
    private final String ip;
    private final int port;
    private DisposableObserver<String> observer;
    private Observable<String> tcpDataObservable;

    public TcpClient2(String ip, int port) {
        this.ip = ip;
        this.port = port;
//        tcpDataObservable = tcpData();
    }

    public Observable<String> connect(){

        if(tcpDataObservable == null){
            final ExecutorService signleExecutor = Executors.newSingleThreadExecutor();
            ConnectableObservable<String> observable = tcpData()
                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(Schedulers.from(signleExecutor))
                    .retryWhen(new TcpClient2.RetryWithDelay(20, 2000))
                    .publish();
            observable.connect();
            tcpDataObservable = observable.ofType(String.class);
        }
        return tcpDataObservable;
    }





    /**
     * 以Observable的方式对外提供TCP数据流
     * */
    private Observable<String> tcpData(){


        Observable<String> tcpObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                BufferedReader in = null;
                try{
                    socket = new Socket(ip,port);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    ObservableEmitter<String> emitter = e;
                    char[] buf;
                    //接收数据这不需要新开线程，由observable的subscribeOn来指定线程
                    while (isConnected){
                        if(isServerClose(socket)){
                            System.out.println("接收异常");
                            throw new Exception("接收异常");
                        }
                        buf = new char[256];
                        int dataRead = in.read(buf,0,256);

                        if(dataRead > 0){
                            String data =  String.valueOf(buf);
                            System.out.println("recevied:" + data + " on " + Thread.currentThread());
                            emitter.onNext(data);
                        }
                        buf = null;

                    }
                }
                finally{

                    if(in != null){
                        in.close();
                    }
                    if(socket != null){
                        socket.close();
                    }
                }
                e.onComplete();

            }
        });

        return tcpObservable;
    }

    public void sendData(String data){
        if(socket != null){
            try {
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                out.write(data);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected()
    {
        if(socket!= null){
            return socket.isConnected();
        }
        return false;
    }

    /**
     * 判断是否断开连接，断开返回true,没有返回false
     * @param socket
     * @return
     */
    private Boolean isServerClose(Socket socket){
        try{
            socket.sendUrgentData(0xFF);//发送1个字节的紧急数据，默认情况下，服务器端没有开启紧急数据处理，不影响正常通信
            return false;
        }catch(Exception se){
            return true;
        }
    }

    /**
     * 出现连接异常后的重连规则
     * */
    public class RetryWithDelay implements Function<Observable<? extends Throwable>, Observable<?>> {

        private final int maxRetries;
        private final int retryDelayMillis;
        private int retryCount;

        public RetryWithDelay(int maxRetries, int retryDelayMillis) {
            this.maxRetries = maxRetries;
            this.retryDelayMillis = retryDelayMillis;
        }

        @Override
        public Observable<?> apply(@NonNull Observable<? extends Throwable> observable) throws Exception {
            return observable
                    .flatMap(new Function<Throwable, ObservableSource<?>>() {
                        @Override
                        public ObservableSource<?> apply(@NonNull Throwable throwable) throws Exception {
                            if (++retryCount <= maxRetries) {
                                // When this Observable calls onNext, the original Observable will be retried (i.e. re-subscribed).
//                                printLog(tvLogs, "", "get error, it will try after " + retryDelayMillis
//                                        + " millisecond, retry count " + retryCount);
                                System.out.println(throwable.getMessage());
                                System.out.println("连接异常，将在"+retryDelayMillis+"毫秒后重连！");
                                return Observable.timer(retryDelayMillis,
                                        TimeUnit.MILLISECONDS);
                            }
                            // Max retries hit. Just pass the error along.
                            return Observable.error(throwable);
                        }
                    });
        }
    }
}
