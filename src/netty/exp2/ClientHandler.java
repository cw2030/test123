package netty.exp2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<String> {
    private final static String LINE_SPLIT = "\r\n";
    private static final String request = "request" + LINE_SPLIT;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("发送请求");
        ByteBuf req = null;
        for(int i = 0; i < 100; i++){
            req = Unpooled.buffer(request.length());
            req.writeBytes(request.getBytes());
            ctx.writeAndFlush(req);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg)
            throws Exception {
        System.out.println(msg);
    }
    
    

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.channel().close();
        super.exceptionCaught(ctx, cause);
    }

    
}
