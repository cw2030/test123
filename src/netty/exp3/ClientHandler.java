package netty.exp3;

import com.google.common.base.Charsets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<String> {
    private final String DELIMITER = "\1";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf req = null;
        for(int i = 0; i < 100; i++){
            req = Unpooled.copiedBuffer(("发送请求request" + DELIMITER).getBytes(Charsets.UTF_8));
            ctx.writeAndFlush(req);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg)
            throws Exception {
        System.out.println(msg);

    }

}
