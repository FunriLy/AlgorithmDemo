package com.qg.fangrui.AlgorithmDemo.NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by funrily on 17-9-26
 * NIO 代码示例
 * @version 1.0.0
 */
public class MultiplexerTimeServer implements Runnable{
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private volatile boolean stop;
    private ByteBuffer readBuffer;

    public MultiplexerTimeServer(int port){
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);   // 将 serverSocketChannel 设置为异步非阻塞模式
            serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); // 将 Channel 注册到 Selector 监听
            System.out.println("时间服务器启动，端口号为：" + port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        while (!stop) { // While 循环体便利 Selector
            try {
                selector.select(1000);  // 设置休眠时间为1s：即 Selector 每隔1秒被唤醒
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey key = null;
                while (iterator.hasNext()) {    // 对所有的 Channel 进行迭代
                    key = iterator.next();
                    iterator.remove();
                    try {
                        System.out.println("网络异步操作");
                        handleInput(key);   // 网络异步操作
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null){
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        if (selector != null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            // 1、处理新接入的请求
            if (key.isAcceptable()){
                // 接收客户端的连接并创建实例
                ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                SocketChannel socketChannel = serverChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_READ);
                // 逻辑处理
                System.out.println("用户实例接入");
            }
            // 2、如果是客户端的读取消息
            if (key.isReadable()) {
                SocketChannel socketChannel = (SocketChannel)key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);  // 创建一个缓冲区
                /* 一般情况下，读取应该放于循环体中，以读取到完整的信息 */
                int readBytes = socketChannel.read(readBuffer); // 将数据读取到缓冲区
                if (readBytes > 0) {        // (1)读取到了字节并进行编解码
                    readBuffer.flip();      // 将缓冲区的标志置于起点
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);  // 将缓冲区内容读取到字节数组
                    String body = new String(bytes, "UTF-8");   // 编码并构造字符串
                    System.out.println("时间服务器接收到字符串：" + body);
                    String currentTime = "QUERY TIME ORDER".equals(body)    // 检查字符串指令
                            ? new Date(System.currentTimeMillis()).toString()
                            : "CAD ORDER";
                    doWrite(socketChannel, currentTime);                    // 异步发送应答消息
                } else if (readBytes < 0){  // (2)链路关闭，需要关闭 SocketChannel 并释放资源
                    key.cancel();
                    socketChannel.close();
                } else {                    // (3)没有读取到字节
                    ;
                }
            }
        }
    }

    /* 将消息异步发送给客户端 */
    private void doWrite(SocketChannel channel, String response) throws IOException {
        System.out.println("调用写方法");
        if (response != null && response.trim().length() > 0) {
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length); // 开辟缓冲区
            writeBuffer.put(bytes);                                     // 填充缓冲区
            writeBuffer.flip();                                         // 重置标记
            channel.write(writeBuffer);                                 // 写出缓冲内容
        }
    }

}
