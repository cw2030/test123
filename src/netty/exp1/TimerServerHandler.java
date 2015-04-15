package netty.exp1;

import com.google.common.primitives.Longs;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TimerServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");
        ctx.channel().close();
        ctx.close();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        System.out.println("channelRead");
        byte[] b = new byte[msg.readableBytes()];
        msg.readBytes(b);
        System.out.println("收到客户端请求：" + new String(b));
        
        ByteBuf resp = Unpooled.copiedBuffer(Longs.toByteArray(System.currentTimeMillis()));
        ctx.write(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        System.out.println("channelReadComplete");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("exceptionCaught");
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegistered");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregistered");
        ctx.channel().close();
    }
    
    
}
