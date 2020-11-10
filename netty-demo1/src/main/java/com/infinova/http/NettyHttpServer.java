package com.infinova.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyHttpServer {

    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ChannelFuture channelFuture = serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .handler(null).childHandler(new HttpChannelInitializer()).bind(6688);
        channelFuture.addListener((future) -> {
            if(future.isSuccess()){
                System.out.println("bind 6668 success");
            }else{
                System.out.println("bind 6668 failed");
            }
        });
    }
}
