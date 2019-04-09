package com.niomultipart;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import com.common.RpcContainer;

public class RpcNioMultClient {

    private static RpcNioMultClient rpcNioClient;

    // 通道管理器
    private Selector selector;

    // 通道
    private SocketChannel channel;

    private String serverIp = "localhost";

    private int port = 8080;

    private RpcNioMultClient() {
        // 初始化client
        initClient();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                listen();
            }
        };
        Thread t = new Thread(runnable);
        t.start();
    }

    public static RpcNioMultClient getInstance() {
        if (rpcNioClient == null) {
            synchronized (RpcNioMultClient.class) {
                if (rpcNioClient == null) {
                    rpcNioClient = new RpcNioMultClient();
                }
            }
        }
        return rpcNioClient;
    }

    public void initClient() {
        try {
            // 打开一个通道
            channel = SocketChannel.open();
            // 设置为非阻塞通道（异步）
            channel.configureBlocking(false);
            // 获得通道管理器，用于监听通道事件
            selector = Selector.open();
            // 建立连接
            channel.connect(new InetSocketAddress(serverIp, port));
            // 由于是非阻塞的，所以有可能连接并未建立完成，调用finishConnect完成连接
            if (channel.isConnectionPending()) {
                channel.finishConnect();
            }
            // 绑定到通道管理器,监听可读事件，因为客户端只需要从服务端获得数据然后读取，所以只需要监听READ事件
            channel.register(selector, SelectionKey.OP_READ);
            System.out.println("客户端初始化完成，建立连接完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        try {
            while (true) {
                // 开始轮询READ事件
                selector.select();
                Iterator ite = selector.selectedKeys().iterator();
                while (ite.hasNext()) {
                    SelectionKey key = (SelectionKey) ite.next();
                    // 删除已选的key,以防重复处理
                    ite.remove();
                    if (key.isReadable()) {
                        // 读取信息
                        readMsgFromServer();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("客户端建立连接失败");
        }
    }

    public boolean sendMsg2Server(byte[] bytes) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length + 4);
            // 放入消息长度，然后放入消息体
            buffer.putInt(bytes.length);
            buffer.put(bytes);
            // 写完之后buffer设置为可读状态
            buffer.flip();
            // 写出消息
            channel.write(buffer);
        } catch (IOException e) {
            System.out.println("客户端写出消息失败！");
            e.printStackTrace();
        }
        return true;
    }

    public void readMsgFromServer() {
        ByteBuffer byteBuffer;
        try {
            // 首先读取请求id
            byteBuffer = ByteBuffer.allocate(8);
            int readIdCount = channel.read(byteBuffer);
            if (readIdCount < 0) {
                System.out.println("fail to read id");
                return;
            }
            byteBuffer.flip();
            Long requsetId = byteBuffer.getLong();

            // 读取返回值长度
            byteBuffer = ByteBuffer.allocate(4);
            int readHeadCount = channel.read(byteBuffer);
            if (readHeadCount < 0) {
                System.out.println("fail to read readHeadCount");
                return;
            }
            // 将buffer切换为待读取状态
            byteBuffer.flip();
            int length = byteBuffer.getInt();

            // 读取消息体
            byteBuffer = ByteBuffer.allocate(length);
            int readBodyCount = channel.read(byteBuffer);
            if (readBodyCount < 0) {
                System.out.println("fail to read data");
                return;
            }
            byte[] bytes = byteBuffer.array();

            // 将返回值放入指定容器
            RpcContainer.addResponse(requsetId, bytes);
        } catch (IOException e) {
            System.out.println("读取数据异常");
            e.printStackTrace();
        }
    }
}