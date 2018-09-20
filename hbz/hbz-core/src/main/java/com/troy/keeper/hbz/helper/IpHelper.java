package com.troy.keeper.hbz.helper;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by leecheng on 2017/10/31.
 */
public class IpHelper {

    public static String getIp() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    String host = inetAddress.getHostAddress();
                    //--System.out.println("枚举：" + host);
                    if (!host.startsWith("169.") && !host.startsWith("127.") && !host.startsWith("0:") && !host.contains(":")) {
                        return host;
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        throw new IllegalStateException();
    }

    public static void main(String[] args) {
        System.out.println(getIp());
    }
}
