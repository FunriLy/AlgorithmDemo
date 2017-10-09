package com.qg.fangrui.AlgorithmDemo.Netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

/**
 * Created by funrily on 17-10-9
 *
 * @version 1.0.0
 */
public class TimeServerHandler extends ChannelHandlerAdapter {

    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        System.out.println("the time server order : " + body+";the counter is :"+ (++counter));
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?new Date(
                System.currentTimeMillis()).toString():"BAD ORDER";
        currentTime +=System.getProperty("line.separator");   // System.getProperty("line.separator")，获取/n的作用
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.writeAndFlush(resp);

        /*
        当客户端和服务端建立了TCP连接成功后，Netty的NIO线程发送查询时间的指令给服务端
        调用ChannelHandlerContext的writeAndFlush方法，将请求消息发送给服务端
        当服务器响应时，channelRead方法被调用
         */
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        ctx.close();
    }
}
