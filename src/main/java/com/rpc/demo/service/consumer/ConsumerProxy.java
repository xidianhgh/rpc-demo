package com.rpc.demo.service.consumer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Proxy;
import java.net.Socket;

public class ConsumerProxy {
    //参考链接
    // https://mp.weixin.qq.com/s?__biz=MzU1ODczNTYxOA==&mid=2247485245&idx=1&sn=7ac8ae4e0ceab9c65edfc8135a3e6f98&chksm=fc234f50cb54c646581ffe7f9703b38f67d465eb3ded2fcb9764d38d5b14819e56d408b95155&mpshare=1&scene=23&srcid=11137Id8tI5Oi9g3TcY2M0v4&sharer_sharetime=1606020969337&sharer_shareid=8c9c2b9105a234c012c6162372806648#rd

    /**
     * 消费者端的动态代理
     * @param interfaceClass 代理的接口类
     * @param host 远程主机IP
     * @param port 远程主机端口
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T consume(final Class<T> interfaceClass,final String host,final int port) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class[]{interfaceClass}, (proxy,method,args) -> {
                    //创建一个客户端套接字
                    Socket socket = new Socket(host, port);
                    try {
                        //创建一个对外传输的对象流，绑定套接字
                        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                        try {
                            //将动态代理的方法名写入对外传输的对象流中
                            output.writeUTF(method.getName());
                            //将动态代理的方法的参数写入对外传输的对象流中
                            output.writeObject(args);
                            //创建一个对内传输的对象流，绑定套接字
                            //这里是为了获取提供者端传回的结果
                            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                            try {
                                //从对内传输的对象流中获取结果
                                Object result = input.readObject();
                                if (result instanceof Throwable) {
                                    throw (Throwable) result;
                                }
                                return result;
                            } finally {
                                input.close();
                            }
                        } finally {
                            output.close();
                        }
                    } finally {
                        socket.close();
                    }
                }
        );
    }
}
