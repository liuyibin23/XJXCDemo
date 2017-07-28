package com.xjxueche.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 刘乙镔 on 2017/7/28.
 */

public class TcpManager2 {
    private static Map<String,TcpClient2> tcpConnections = new HashMap<>();

    public static TcpClient2 getTcpClient(String ip,int port){
        String ipPort = ip + ":" + Integer.toString(port);
        if(tcpConnections.containsKey(ipPort)){
            return tcpConnections.get(ipPort);
        }
        TcpClient2 tcpClient = new TcpClient2(ip, port);
        tcpConnections.put(ipPort,tcpClient);
        return tcpClient;
    }
}
