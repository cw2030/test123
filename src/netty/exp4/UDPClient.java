package netty.exp4;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

public class UDPClient {

    public static void main(String[] args) {
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap client = new Bootstrap();
            client.group(worker)
            .channel(NioDatagramChannel.class)
            .option(ChannelOption.SO_BROADCAST, true)
            .handler(new ClientHandler());
            Channel channel = client.bind(0).sync().channel();
            channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("谚语字典查询?", CharsetUtil.UTF_8), 
                                                     new InetSocketAddress("255.255.255.255", 12345))).sync();
            channel.closeFuture().await();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            worker.shutdownGracefully();
        }

    }

}
