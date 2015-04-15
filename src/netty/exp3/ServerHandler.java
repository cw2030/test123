package netty.exp3;

import java.util.concurrent.atomic.AtomicInteger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<String> {
    private static final AtomicInteger count = new AtomicInteger();
    private static final String DELIMITER = "\1";
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg)
            throws Exception {
        System.out.println("收到客户端请求：" + msg);
        ByteBuf resp = Unpooled.copiedBuffer(("返回客户端RESP:" + count.incrementAndGet() + DELIMITER).getBytes());
        ctx.writeAndFlush(resp);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregistered");
        ctx.close();
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }
    

}
