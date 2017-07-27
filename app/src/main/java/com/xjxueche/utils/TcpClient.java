package com.xjxueche.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import kotlin.jvm.Throws;

/**
 * Created by 刘乙镔 on 2017/7/26.
 */

public class TcpClient {

    private ObservableOnSubscribe<String> subscribe;
    private Socket socket;
    private boolean isConnected = true;

    public  Observable<String> tcpData(final String ip,final int port) throws Exception {

//        socket = new Socket(ip,port);
//        final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        final PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);


        Observable<String> tcpObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                BufferedReader in = null;
                PrintWriter out = null;
                try{
                    socket = new Socket(ip,port);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    ObservableEmitter<String> emitter = e;
                    char[] buf = new char[256];
                    //接收数据这不需要新开线程，由observable的subscribeOn来指定线程
                    while (isConnected){
                        if(isServerClose(socket)){
                            System.out.println("接收异常");
                            throw new Exception("接收异常");
                        }

                        int dataRead = in.read(buf,0,256);
                        String data =  String.valueOf(buf);
                        emitter.onNext(data);
                        System.out.println("recevied:" + data + " on " + Thread.currentThread());
                    }
                }
                finally{

                    if(out != null){
                        out.flush();
                        out.close();
                    }
                    if(in != null){
                        in.close();
                    }
                    if(socket != null){
                        socket.close();
                    }
                }
                e.onComplete();

//                //接收数据这不需要新开线程，由observable的subscribeOn来指定线程
//                for (int i = 0; i < 50; i++) {
//                    emitter.onNext(Integer.toString(i));
//                    System.out.println("recevied:" + Integer.toString(i) + " on " + Thread.currentThread());
////                    if(i == 10)
////                    {
////                        throw new Exception("接收异常");
////                    }
//                    Thread.sleep(200);
//                }

            }
        });

        return tcpObservable;
    }

    /**
     * 判断是否断开连接，断开返回true,没有返回false
     * @param socket
     * @return
     */
    public Boolean isServerClose(Socket socket){
        try{
            socket.sendUrgentData(0xFF);//发送1个字节的紧急数据，默认情况下，服务器端没有开启紧急数据处理，不影响正常通信
            return false;
        }catch(Exception se){
            return true;
        }
    }

    private void sendOrder(String order,Socket socket) throws IOException {
//        try {
////            out.write(order.getBytes("UTF-8"));
//            out.write(order);
//            out.flush();
////            return true;
//        } catch (UnsupportedEncodingException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
////            return false;
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
////            return false;
//        }
        OutputStream out = socket.getOutputStream();
        out.write(order.getBytes("UTF-8"));
        out.flush();

    }
}
