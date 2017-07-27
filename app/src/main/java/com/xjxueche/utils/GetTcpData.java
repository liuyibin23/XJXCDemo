package com.xjxueche.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by 刘乙镔 on 2017/7/26.
 */

public class GetTcpData extends UseCase<String,GetTcpData.Params>{

//    val signleExecutor : ExecutorService = Executors.newSingleThreadExecutor()

    private final ExecutorService signleExecutor = Executors.newSingleThreadExecutor();

    public GetTcpData(Scheduler subscribeScheduler, Scheduler observeScheduler){
//        Thread.currentThread().
        super(subscribeScheduler,observeScheduler);
    }

    @Override
    Observable<String> buildUseCaseObservable(GetTcpData.Params ipAndPort) {
        try {
            return new TcpClient().tcpData(ipAndPort.ip,ipAndPort.port);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void publish(DisposableObserver<String> observer, GetTcpData.Params ipAndPort) {
        //Hot observables
        ConnectableObservable<String> observable = this.buildUseCaseObservable(ipAndPort)
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler).retryWhen(new RetryWithDelay(20, 2000)).publish();
        observable.connect();
        addDisposable(observable.subscribeWith(observer));
    }
    /**
     * 出现连接异常后的重连规则
     * */
    public class RetryWithDelay implements Function<Observable<? extends Throwable>, Observable<?>>{

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

    public final static class  Params{
        private final String ip;
        private final int port;

        Params(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        public static Params ipAndPort(String ip, int port){ return new Params(ip,port);}
    }

}
