package com.ganghuan.register;

import com.ganghuan.loadbalance.LoadBalance;
import com.ganghuan.loadbalance.RandomLoadBalance;
import com.ganghuan.loadbalance.RoundLoadBalance;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.util.List;

public class ZKServiceRegister implements ServiceRegister {
    // curator提供的zookeeper客户端
    private CuratorFramework client;
    // zookeeper根路径节点
    private static final String ROOT_PATH = "MyRPC";
    // 初始化负载均衡器，这里用的是轮询，一般通过构造函数传入
//    private LoadBalance loadBalance = new RandomLoadBalance();
    private LoadBalance loadBalance = new RoundLoadBalance();

    // 这里负责zookeeper客户端的初始化，并与zookeeper服务端建立连接
    public ZKServiceRegister() {
        // 指数时间重试
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
        /**
         * zookeeper的地址固定，不管是服务提供者还是消费者都要与之建立连接
         * sessionTimeoutMs 与 zoo.cfg中的tickTime有关系
         * zk还会根据minSessionTimeout与maxSessionTimeout两个参数重新调整最后的超时值，默认分别为tickTime的2倍和20倍
         * 使用心跳监听状态
         */
        this.client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                .sessionTimeoutMs(40000)
                .retryPolicy(policy)
                .namespace(ROOT_PATH).build();
        this.client.start();
        System.out.println("zookeeper 连接成功");
    }

    @Override
    public void register(String serviceName, InetSocketAddress serverAddress) {
        try {
            // serviceName创建成永久节点，服务提供者下线时，不删服务名，只删地址
            if (client.checkExists().forPath("/" + serviceName) == null) {
                client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath("/" + serviceName);
            }
            // 路径地址，一个'/'代表一个节点
            String path = "/" + serviceName + "/" + getServiceAddress(serverAddress);
            // 临时节点，服务器下线就删除节点
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path);
        } catch (Exception e) {
            System.out.println("此服务已存在");
        }
    }

    // 根据服务名返回地址
    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {
        try {
            // Version 5
//            List<String> strings = client.getChildren().forPath("/" + serviceName);
//            // 这里默认用第一个，后面加负载均衡
//            String string = strings.get(0);

            // Version 6
            List<String> strings = client.getChildren().forPath("/" + serviceName);
            // 负载均衡选择器
            String string = loadBalance.balance(strings);
            return parseAddress(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 地址 -> xxx.xxx.xxx.xxx:port 字符串
    private String getServiceAddress(InetSocketAddress serverAddress) {
        return serverAddress.getHostName() + ":" + serverAddress.getPort();
    }

    // 字符串解析为地址
    private InetSocketAddress parseAddress(String address) {
        String[] result = address.split(":");
        return new InetSocketAddress(result[0], Integer.parseInt(result[1]));
    }
}
