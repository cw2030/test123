package netty.exp2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.base.Charsets;

public class ServerHandler extends SimpleChannelInboundHandler<String> {
    
    private final static String LINE_SPLIT = "\r\n";
    private static final AtomicInteger count = new AtomicInteger();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg)
            throws Exception {
        System.out.println(msg);
        
        ByteBuf resp = Unpooled.copiedBuffer(("RESP:" + count.incrementAndGet()+ LINE_SPLIT).getBytes(Charsets.UTF_8) );
        ctx.writeAndFlush(resp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }
    
    

}
