package com.ganghuan.server;

import com.ganghuan.service.BlogService;
import com.ganghuan.service.BlogServiceImpl;
import com.ganghuan.service.UserService;
import com.ganghuan.service.UserServiceImpl;

public class TestServer {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        BlogService blogService = new BlogServiceImpl();

        // Version 1
//        Map<String, Object> serviceProvide = new HashMap<>();
//        // 暴露两个服务接口，即在RPCServer中加一个HashMap
//        serviceProvide.put("com.ganghuan.service.UserService", userService);
//        serviceProvide.put("com.ganghuan.service.BlogService", blogService);

        // Version 2
//        ServiceProvider serviceProvider = new ServiceProvider();
//        serviceProvider.provideServiceInterface(userService);
//        serviceProvider.provideServiceInterface(blogService);

        ServiceProvider serviceProvider = new ServiceProvider("127.0.0.1", 8899);
        serviceProvider.provideServiceInterface(userService);
        serviceProvider.provideServiceInterface(blogService);

        RPCServer rpcServer = new NettyRPCServer(serviceProvider);
        rpcServer.start(8899);
    }
}
