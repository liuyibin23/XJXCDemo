package com.xjxueche.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 刘乙镔 on 2017/7/27.
 */

public class TcpManager {
    private static Map<String,TcpClient> tcpConnections = new HashMap<>();

    public static TcpClient getTcpClient(String ip,int port){
        String ipPort = ip + ":" + Integer.toString(port);
        if(tcpConnections.containsKey(ipPort)){
            return tcpConnections.get(ipPort);
        }
        TcpClient tcpClient = new TcpClient(ip, port);
        tcpConnections.put(ipPort,tcpClient);
        return tcpClient;
    }

    public static GetTcpData tcpDataSource(){
        ExecutorService signleExecutor = Executors.newSingleThreadExecutor();
//        Schedulers.trampoline()
        GetTcpData tcpData = new GetTcpData(Schedulers.newThread(), Schedulers.from(signleExecutor));
//        GetTcpData tcpData = new GetTcpData(Schedulers.newThread(),Schedulers.trampoline());
        return tcpData;
    }

//    public static void connectTcp(String ip,int port){
//        ExecutorService signleExecutor = Executors.newSingleThreadExecutor();
////        Schedulers.trampoline()
//        GetTcpData tcpData = new GetTcpData(Schedulers.newThread(),Schedulers.from(signleExecutor));
////        GetTcpData tcpData = new GetTcpData(Schedulers.newThread(),Schedulers.trampoline());
//
//        tcpData.publish(new DisposableObserver<String>() {
//            @Override
//            public void onNext(@NonNull String data) {
//                //处理TCP数据
//                System.out.println("process:" + data + " on " + Thread.currentThread());
//            }
//
//            @Override
//            public void onError(@NonNull Throwable e) {
//                //处理连接断开且超过重连次数
//                System.out.println("接收数据发生异常");
//            }
//
//            @Override
//            public void onComplete() {
//                //连接正常断开
//                System.out.println("接收数据完成！");
//            }
//        },GetTcpData.Params.ipAndPort("127.0.0.1",4001));
//    }

}
