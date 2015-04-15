package netty.exp2;

import com.google.common.base.Charsets;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class LineBaseFrameClient {

    public static void main(String[] args) {
        EventLoopGroup worker = new NioEventLoopGroup();
        Bootstrap client = null;
        try{
            client = new Bootstrap();
            client.group(worker);
            client.channel(NioSocketChannel.class);
            client.option(ChannelOption.SO_KEEPALIVE, true);
            client.option(ChannelOption.SO_REUSEADDR, true);
            client.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline cp = ch.pipeline();
                    cp.addLast(new LineBasedFrameDecoder(1024));
                    cp.addLast(new StringDecoder(Charsets.UTF_8));
                    cp.addLast(new ClientHandler());
                }
                
            });
            
            ChannelFuture cf = client.connect("localhost", 12345).sync();
            cf.channel().closeFuture().await();
            System.out.println("客户端关闭");
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            worker.shutdownGracefully();
        }

    }

}
