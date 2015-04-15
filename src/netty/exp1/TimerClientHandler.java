package netty.exp1;

import com.google.common.primitives.Longs;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TimerClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    String request = "timer";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg)
            throws Exception {
        System.out.println("channelRead0");
        byte[] b = new byte[msg.readableBytes()];
        msg.readBytes(b);
        System.out.println("收到服务端返回：" + Longs.fromByteArray(b));
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
        System.out.println("isOpen:" + ctx.channel().isOpen());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");
        ByteBuf resp = Unpooled.copiedBuffer(request.getBytes());
        ctx.writeAndFlush(resp);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelReadComplete");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        System.out.println("exceptionCaught");
        ctx.close();
    }

}
