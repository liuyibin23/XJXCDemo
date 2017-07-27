package com.xjxueche.xjxcdemo;

import com.xjxueche.utils.GetTcpData;
import com.xjxueche.utils.TcpClient;
import com.xjxueche.utils.TcpManager;
import com.xjxueche.utils.UseCase;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
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
}
