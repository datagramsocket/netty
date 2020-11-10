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

public class GroupChatChannelHandler extends SimpleChannelInboundHandler<String> {

    DefaultChannelGroup defaultChannelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress socketAddress = channel.remoteAddress();

        for(Channel ch : defaultChannelGroup){
            ch.writeAndFlush(Unpooled.copiedBuffer("客户端：" + socketAddress + "上线", CharsetUtil.UTF_8));
        }
        defaultChannelGroup.add(channel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress socketAddress = channel.remoteAddress();
        for(Channel ch : defaultChannelGroup){
            if(ch != ctx.channel()){
                ch.writeAndFlush("客户端" + socketAddress.toString() + ":" + msg);
            }
        }
    }



    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress socketAddress = channel.remoteAddress();

        for(Channel ch : defaultChannelGroup){
            ch.writeAndFlush(Unpooled.copiedBuffer("客户端：" + socketAddress + "下线", CharsetUtil.UTF_8));
        }
    }
}
