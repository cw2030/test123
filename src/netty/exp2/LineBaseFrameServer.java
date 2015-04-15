package netty.exp2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import com.google.common.base.Charsets;

/**
 * 以换行符\r\n为结束符对发送数据进行截取<br>
 * <b>因此需要在发送数据结束加上换行符，否则服务端收不到客户端的数据，客户端也收不到服务器返回的数据</b>
 * @author user
 *
 */
public class LineBaseFrameServer {

    public static void main(String[] args) {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try{
            ServerBootstrap server = new ServerBootstrap();
            server.group(boss, worker);
            server.channel(NioServerSocketChannel.class);
            server.option(ChannelOption.TCP_NODELAY, true);
            server.option(ChannelOption.SO_BACKLOG, 120);
            server.option(ChannelOption.SO_KEEPALIVE, true);
            server.option(ChannelOption.SO_REUSEADDR, true);
            server.handler(new LoggingHandler(LogLevel.INFO));
            server.childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline cp = ch.pipeline();
                    cp.addLast(new LineBasedFrameDecoder(1024));
                    cp.addLast(new StringDecoder(Charsets.UTF_8));
                    cp.addLast(new ServerHandler());
                    
                }
                
            });
            ChannelFuture cf = server.bind(12345).sync();
            System.out.println("监听12345端口");
            cf.channel().closeFuture().await();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

}
