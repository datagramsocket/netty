package com.infinova.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class GroupChatClient {

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            bootstrap.group(workerGroup).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new GroupChatChannelHander());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6559).sync();
            channelFuture.addListener(future -> {
                if(future.isSuccess()){
                    System.out.println("client connect success");
                }else{
                    System.out.println("client connect failed");
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            workerGroup.shutdownGracefully();
        }

    }
}
