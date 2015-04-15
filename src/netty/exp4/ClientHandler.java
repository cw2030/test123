package netty.exp4;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

public class ClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    
    private final AtomicInteger count = new AtomicInteger();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg)
            throws Exception {
        String resp = msg.content().toString(CharsetUtil.UTF_8);
        System.out.println(resp);
        if(count.incrementAndGet() >= 10){
            ctx.close();
        }else{
            ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("谚语字典查询?", CharsetUtil.UTF_8), 
                                                 new InetSocketAddress("255.255.255.255", 12345))).sync();
        }
        
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
        super.channelReadComplete(ctx);
    }
    

}
