package com.infinova.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class MyHttpChannelHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof HttpRequest){
            System.out.println(msg.getClass());
            HttpRequest httpRequest = (HttpRequest)msg;
            System.out.println(httpRequest.uri());
            System.out.println(ctx.pipeline().hashCode());
            System.out.println(ctx.channel().hashCode());
            ByteBuf byteBuf = Unpooled.copiedBuffer("this is netty http server", CharsetUtil.UTF_8);
            HttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain")
                                    .set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
            ctx.writeAndFlush(httpResponse);



        }
    }
}
