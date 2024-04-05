package com.ganghuan.server;

import com.ganghuan.service.BlogService;
import com.ganghuan.service.BlogServiceImpl;
import com.ganghuan.service.UserService;
import com.ganghuan.service.UserServiceImpl;

public class TestServer2 {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        BlogService blogService = new BlogServiceImpl();

        ServiceProvider serviceProvider = new ServiceProvider("127.0.0.1", 8900);
        serviceProvider.provideServiceInterface(userService);
        serviceProvider.provideServiceInterface(blogService);

        RPCServer rpcServer = new NettyRPCServer(serviceProvider);
        rpcServer.start(8900);
    }
}
