package com.qg.fangrui.AlgorithmDemo.NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by funrily on 17-9-26
 * 时间客户端处理类
 * @version 1.0.0
 */
public class TimeClientHandle implements Runnable {

    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean stop;

    public TimeClientHandle(String host, int port) {
        this.host = host==null ? "127.0.0.1" : host;
        this.port = port;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
            doConnect();        // 发起连接
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        while (!stop) {
            try {
                selector.select(1000);  // 每隔1s唤醒一次
                Set<SelectionKey> selectionKeys = selector.selectedKeys();  // 获得所有的 SelectionKey
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey key = null;
                while (iterator.hasNext()) {    // 遍历
                    key = iterator.next();      // 得到 key 值
                    iterator.remove();
                    try {
                        handleInput(key);       // 重要处理方法
                    } catch (IOException e) {
                        if (key != null){
                            key.cancel();
                            if (key.channel() != null){
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            SocketChannel channel = (SocketChannel) key.channel();
            if (key.isConnectable()) {
                if (channel.finishConnect()) {
                    channel.register(selector, SelectionKey.OP_READ);
                    doWrite(channel);
                } else {
                    System.exit(1); // 连接失败，退出进程
                }
            }

            if (key.isReadable()) {
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = channel.read(readBuffer);
                if (readBytes > 0) {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("接到的信息：" + body);
                    this.stop = true;           // 优雅退出循环
                } else if (readBytes < 0) {
                    key.cancel();
                    socketChannel.close();  // 关闭链接
                } else {
                    ;   // 读到0字节
                }
            }
        }
    }

    private void doConnect() throws IOException {
        if (socketChannel.connect(new InetSocketAddress(host, port))) {
            socketChannel.register(selector, SelectionKey.OP_READ); // 连接成功，将 SocketChannel 注册到多路复用器
            // 监听读操作
            doWrite(socketChannel); // 发送请求
        } else {
            // 如果没有应答消息，并不代表连接失败
            socketChannel.register(selector, SelectionKey.OP_CONNECT);  // 监听连接
        }
    }

    private void doWrite(SocketChannel channel) throws IOException {
        System.out.println("调用写方法");
        byte[] req = "QUERY TIME ORDER".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);   // 分配缓冲去
        writeBuffer.put(req);                                       // 填充缓冲区
        writeBuffer.flip();                                         // 重置标记
        channel.write(writeBuffer);                                 // 写入通道
        if (!writeBuffer.hasRemaining()) {                          // 结果判断
            System.out.println("客户端成功发送信息给服务器");
        }
    }
}
