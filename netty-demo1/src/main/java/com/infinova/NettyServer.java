package com.infinova;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty server
 * */
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        /*.option(ChannelOption.SO_BACKLOG, 128)
                        .childOption(ChannelOption.SO_KEEPALIVE, true)*/
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast(new NettyServerHandler());
                            }
                        });
        ChannelFuture channelFuture = serverBootstrap.bind(6668);
       /* channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println(channelFuture == future);
                if(future.isSuccess()){
                    System.out.println("bind 6668 success ");
                }
            }
        });*/

       //lambda写法
       channelFuture.addListener((future) -> {
           if(future.isSuccess()){
               System.out.println("bind 6668 success ");
           }else{
               System.out.println("bind 6668 failed ");
           }
       });

        channelFuture.channel().closeFuture().sync();

    }
}
