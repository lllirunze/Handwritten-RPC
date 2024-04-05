package com.ganghuan.client;

import com.ganghuan.common.RPCRequest;
import com.ganghuan.common.RPCResponse;
import com.ganghuan.register.ServiceRegister;
import com.ganghuan.register.ZKServiceRegister;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

@AllArgsConstructor
public class SimpleRPCClient implements RPCClient {
    private String host;
    private int port;

    private ServiceRegister serviceRegister;

    public SimpleRPCClient() {
        // 初始化注册中心，建立连接
        this.serviceRegister = new ZKServiceRegister();
    }

    // 客户端发起一次请求调用，Socket建立连接，发起请求Request，得到响应Response
    // 这里的request是封装好的，不同的service需要进行不同的封装， 客户端只知道Service接口，需要一层动态代理根据反射封装不同的Service
    @Override
    public RPCResponse sendRequest(RPCRequest request) {
        // Version 3
//        try {
//            Socket socket = new Socket(host, port);
//
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
//
//            System.out.println(request);
//            objectOutputStream.writeObject(request);
//            objectOutputStream.flush();
//
//            RPCResponse response = (RPCResponse) objectInputStream.readObject();
//
//            return response;
//        } catch (IOException | ClassNotFoundException e) {
//            System.out.println();
//            return null;
//        }

        // Version 5
        InetSocketAddress address = serviceRegister.serviceDiscovery(request.getInterfaceName());
        host = address.getHostName();
        port = address.getPort();

        try {
            Socket socket = new Socket(host, port);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            System.out.println(request);
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();

            RPCResponse response = (RPCResponse) objectInputStream.readObject();
            return response;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println();
            return null;
        }
    }

}
