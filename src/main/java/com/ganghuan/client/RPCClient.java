package com.ganghuan.client;

import com.ganghuan.common.Blog;
import com.ganghuan.common.RPCRequest;
import com.ganghuan.common.RPCResponse;
import com.ganghuan.common.User;
import com.ganghuan.service.BlogService;
import com.ganghuan.service.UserService;

// Version 2
//public class RPCClient {
//    public static void main(String[] args) {
//        // Version 0;
////        try {
////            // 建立Socket连接
////            Socket socket = new Socket("127.0.0.1", 8899);
////            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
////            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
////            // 传给服务器id
////            objectOutputStream.writeInt(new Random().nextInt());
////            objectOutputStream.flush();
////            // 服务器查询数据，返回对应的对象
////            User user = (User)objectInputStream.readObject();
////            System.out.println("服务端返回的User: " + user);
////        } catch (IOException | ClassNotFoundException e) {
////            e.printStackTrace();
////            System.out.println("客户端启动失败");
////        }
//
//        // Version 1
//        RPCClientProxy clientProxy = new RPCClientProxy("127.0.0.1", 8899);
//        UserService proxy = clientProxy.getProxy(UserService.class);
//        // 服务的方法1
//        User userByUserId = proxy.getUserByUserId(10);
//        System.out.println("从服务端得到的user为: " + userByUserId);
//        // 服务的方法2
//        User user = User.builder().userName("张三").id(100).sex(true).build();
//        Integer integer = proxy.insertUserId(user);
//        System.out.println("向服务端插入数据: " + integer);
//
//        // Version2
//        BlogService blogService = clientProxy.getProxy(BlogService.class);
//        Blog blogById = blogService.getBlogById(10000);
//        System.out.println("从服务端得到的blog为: " + blogById);
//    }
//}

// Version 3
public interface RPCClient {
    RPCResponse sendRequest(RPCRequest request);
}