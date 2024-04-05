package com.ganghuan.server;

// Version 1
//import com.ganghuan.common.RPCRequest;
//import com.ganghuan.common.RPCResponse;
//import com.ganghuan.common.User;
//import com.ganghuan.service.UserServiceImpl;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//public class RPCServer {
//    public static void main(String[] args) {
//        UserServiceImpl userService = new UserServiceImpl();
//        try {
//            ServerSocket serverSocket = new ServerSocket(8899);
//            System.out.println("服务端启动了");
//            // BIO的方式监听socket
//            while (true) {
//                Socket socket = serverSocket.accept();
//                // 开启一个线程去处理
//                new Thread(() -> {
//                    try {
//                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
//
//                        // Version 0
////                        // 读取客户端传过来的id
////                        Integer id = ois.readInt();
////                        User userByUserId = userService.getUserByUserId(id);
////                        // 写入User对象给客户端
////                        oos.writeObject(userByUserId);
////                        oos.flush();
//
//                        // Version 1
//                        // 读取客户端传过来的request
//                        RPCRequest request = (RPCRequest)ois.readObject();
//                        // 反射调用对应方法
//                        Method method = userService.getClass().getMethod(request.getMethodName(), request.getParamsTypes());
//                        Object invoke = method.invoke(userService, request.getParams());
//                        // 封装，写入response对象
//                        oos.writeObject(RPCResponse.success(invoke));
//                        oos.flush();
//                    } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
//                             InvocationTargetException e) {
//                        e.printStackTrace();
//                        System.out.println("从IO中读取数据错误");
//                    }
//                }).start();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("服务器启动失败");
//        }
//    }
//}

// Version 2
public interface RPCServer {
    void start(int port);
    void stop();
}