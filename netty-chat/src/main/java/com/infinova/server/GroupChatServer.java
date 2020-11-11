package com.infinova.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class GroupChatServer {
    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new StringDecoder())
                                    .addLast(new StringEncoder())
                                    .addLast(new GroupChatChannelHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(6559).sync();
            System.out.println("main id: " + Thread.currentThread().getId());
            channelFuture.addListener(future -> {
               if(future.isSuccess()){
                   System.out.println("future id : " + Thread.currentThread().getId());
                   System.out.println("server bind success");
               }else{
                   System.out.println("server bind failed");
               }
            });
            channelFuture.channel().closeFuture().sync();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("shutdownGracefully");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
