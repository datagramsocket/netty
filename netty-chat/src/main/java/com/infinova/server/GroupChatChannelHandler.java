package com.infinova.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class GroupChatChannelHandler extends SimpleChannelInboundHandler<String> {

   static List<Channel> channelList = new ArrayList<>();



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("客户端" + channel.remoteAddress() + "上线");
        for(Channel ch : channelList){
            ch.writeAndFlush("客户端：" + channel.remoteAddress() + "上线");
        }
        channelList.add(channel);
    }



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        Channel channel = ctx.channel();
        SocketAddress socketAddress = channel.remoteAddress();
        System.out.println("客户端" + socketAddress + ":" + msg);
        for(Channel ch : channelList){
            if(ch != ctx.channel()){
                ch.writeAndFlush("客户端" + socketAddress.toString() + ":" + msg);
            }
        }
    }



    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelList.remove(channel);
        SocketAddress socketAddress = channel.remoteAddress();
        System.out.println("客户端：" + socketAddress + "下线");
        for(Channel ch : channelList){
            ch.writeAndFlush("客户端：" + socketAddress + "下线");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }


}
