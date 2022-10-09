package com.tulun.nettytest;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * @author QiangQin
 * * @date 2021/7/31
 */
public class Client {
    public static void main(String[] args) throws InterruptedException {
        //创建 事件循环组(仅需一个)
        NioEventLoopGroup loopGroup = new NioEventLoopGroup();

        //创建启动辅助类
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(loopGroup)
                .channel(NioSocketChannel.class) // 客户端
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //获取pipeline的实例
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        //自定义channelHandler ：用来接收服务端返回的消息
                        pipeline.addLast(new ClientHandler());
                    }
                });

      /*  客户端连接服务端
         connect本身是非阻塞的，这里通过sync同步方式来保证 连接成功；
         一定要拿到channel()实例，才能给发消息*/
        Channel channel = bootstrap.connect("127.0.0.1", 9999).sync().channel();

        //给服务端通信发送消息
        /*
            由于Scanner对象将首先跳过输入流开头的所有空白分隔符，然后对输入流中的信息进行检查，直到遇到空白分隔符为止；
            因此输入数据: abc def；空白后面的字符：def，不会显示输出来。
        * */
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n"); //将分隔符 设置为 回车，默认是空格
        while (true) {
            String msg = scanner.nextLine();
            if ("exit".equals(msg))
                break;
            //发送消息给服务端
            channel.writeAndFlush(msg);
        }

        channel.closeFuture().sync();
    }
}


