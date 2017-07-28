package com.xjxueche.xjxcdemo;

import com.xjxueche.utils.GetTcpData;
import com.xjxueche.utils.TcpClient;
import com.xjxueche.utils.TcpClient2;
import com.xjxueche.utils.TcpManager;
import com.xjxueche.utils.TcpManager2;
import com.xjxueche.utils.UseCase;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 刘乙镔 on 2017/7/26.
 */

public class UseCaseTest {

    @Test
    public void GetTcpDataTest()throws Exception{
//        ExecutorService signleExecutor = Executors.newSingleThreadExecutor();
//        Schedulers.trampoline()
        GetTcpData tcpData = TcpManager.tcpDataSource();
//        GetTcpData tcpData = new GetTcpData(Schedulers.newThread(),Schedulers.trampoline());

        tcpData.publish(new DisposableObserver<String>() {
            @Override
            public void onNext(@NonNull String data) {
                //处理TCP数据
                System.out.println("process:" + data + " on " + Thread.currentThread());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                //处理连接断开且超过重连次数
                System.out.println("接收数据发生异常");
            }

            @Override
            public void onComplete() {
                //连接正常断开
                System.out.println("接收数据完成！");
            }
        },GetTcpData.Params.ipAndPort("127.0.0.1",4001));



        Thread.sleep(120000);
        tcpData.dispose();
    }

    @Test
    public void TcpManageTest()throws Exception{

        GetTcpData tcpData = TcpManager.tcpDataSource();
        tcpData.publish(new DisposableObserver<String>() {
            @Override
            public void onNext(@NonNull String data) {
                //处理TCP数据
                System.out.println("process:" + data + " on " + Thread.currentThread());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                //处理连接断开且超过重连次数
                System.out.println("接收数据发生异常");
            }

            @Override
            public void onComplete() {
                //连接正常断开
                System.out.println("接收数据完成！");
            }
        },GetTcpData.Params.ipAndPort("127.0.0.1",4000));
        Thread.sleep(3000);
        TcpClient client = TcpManager.getTcpClient("127.0.0.1",4000);
        client.sendData("test1");
        client.sendData("test2");
        client.sendData("test3");
        client.sendData("test4");
        client.sendData("test5");
        client.sendData("test6");
        Thread.sleep(60000);
        tcpData.dispose();
    }

    @Test
    public void TcpClient2Test()throws Exception{
        TcpClient2 tcpClient2 = new TcpClient2("127.0.0.1",4000);
        final ExecutorService signleExecutor = Executors.newSingleThreadExecutor();
        tcpClient2.connect()
                .observeOn(Schedulers.from(signleExecutor))
                .subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull String data) {
                //处理TCP数据
                System.out.println("process:" + data + " on " + Thread.currentThread());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                //处理连接断开且超过重连次数
                System.out.println("接收数据发生异常");
            }

            @Override
            public void onComplete() {
                //连接正常断开
                System.out.println("接收数据完成！");
            }
        });

        tcpClient2.connect().subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String data) throws Exception {
                //验证多处注册，处理TCP数据
                System.out.println("222222222222process:" + data + " on " + Thread.currentThread());
            }
        });

        Thread.sleep(3000);
        tcpClient2.sendData("test1");
        tcpClient2.sendData("test2");
        tcpClient2.sendData("test3");
        tcpClient2.sendData("test4");
        tcpClient2.sendData("test5");
        tcpClient2.sendData("test6");
        Thread.sleep(120000);
    }


    @Test
    public void TcpManager2Test() throws Exception{
        final ExecutorService signleExecutor = Executors.newSingleThreadExecutor();
        TcpManager2.getTcpClient("127.0.0.1",4001).connect()
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String rawData) throws Exception {
                        //在此处处理原始数据
                        String parsedData = rawData + "     parsedData";
                        return parsedData;
                    }
                })
                .observeOn(Schedulers.from(signleExecutor))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String data) {
                        //处理TCP数据
                        System.out.println("process:" + data + " on " + Thread.currentThread());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        //处理连接断开且超过重连次数
                        System.out.println("接收数据发生异常");
                    }

                    @Override
                    public void onComplete() {
                        //连接正常断开
                        System.out.println("接收数据完成！");
                    }
                });

        TcpManager2.getTcpClient("127.0.0.1",4001).connect()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String data) throws Exception {
                        //验证多处注册，处理TCP数据
                        System.out.println("222222222222process:" + data + " on " + Thread.currentThread());
                    }
                });
        Thread.sleep(3000);
        TcpManager2.getTcpClient("127.0.0.1",4001).sendData("test1");
        TcpManager2.getTcpClient("127.0.0.1",4001).sendData("test2");
        TcpManager2.getTcpClient("127.0.0.1",4001).sendData("test3");
        TcpManager2.getTcpClient("127.0.0.1",4001).sendData("test4");
        TcpManager2.getTcpClient("127.0.0.1",4001).sendData("test5");
        TcpManager2.getTcpClient("127.0.0.1",4001).sendData("test6");
        Thread.sleep(120000);

    }

}
