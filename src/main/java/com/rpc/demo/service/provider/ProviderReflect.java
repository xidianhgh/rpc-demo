package com.rpc.demo.service.provider;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProviderReflect {

    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * RPC监听和远程方法调用
     * @param service RPC远程方法调用的接口实例
     * @param port 监听的端口
     * @throws Exception
     */
    public static void provider(final Object service,int port) throws Exception {
        //创建服务端的套接字，绑定端口port
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            //开始接收客户端的消息，并以此创建套接字
            final Socket socket = serverSocket.accept();
            //多线程执行，这里的问题是连接数过大，线程池的线程数会耗尽
            executorService.execute(() -> {
                try {
                    //创建呢一个对内传输的对象流，并绑定套接字
                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                    try {
                        try {
                            //从对象流中读取接口方法的方法名
                            String methodName = input.readUTF();
                            //从对象流中读取接口方法的所有参数
                            Object[] args = (Object[]) input.readObject();
                            Class[] argsTypes = new Class[args.length];
                            for (int i = 0;i < args.length;i++) {
                                argsTypes[i] = args[i].getClass();

                            }
                            //创建一个对外传输的对象流，并绑定套接字
                            //这里是为了将反射执行结果传递回消费者端
                            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                            try {
                                Class<?>[] interfaces = service.getClass().getInterfaces();
                                Method method = null;
                                for (int i = 0;i < interfaces.length;i++) {
                                    method = interfaces[i].getDeclaredMethod(methodName,argsTypes);
                                    if (method != null) {
                                        break;
                                    }
                                }
                                // ☆★☆★☆★☆★☆★☆★☆★
                                // 使用以下反射来执行方法，需要继承接口
//                                Object result = method.invoke(service, args);
                                // 使用以下反射来执行方法，则不需要继承接口
                                Method executedMethod = service.getClass().getMethod(methodName, String.class);
                                Object invokeResult = executedMethod.invoke(service, args);
                                //对象也可以是以new出来的形式
//                                Object invokeResult = executedMethod.invoke(new HelloProviderServiceImpl(), args);

                                //将反射执行结果写入对外传输的对象流中
                                output.writeObject(invokeResult);
                            } catch (Throwable t) {
                                output.writeObject(t);
                            } finally {
                                output.close();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            input.close();
                        }
                    } finally {
                        socket.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
