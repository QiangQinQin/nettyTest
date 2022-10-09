package com.tulun.nettytest;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author QiangQin
 * * @date 2021/7/31
 */
public class Server {
    public static void main(String[] args) {
        NioEventLoopGroup boss = null;
        NioEventLoopGroup worker = null;

        try {
            //需要eventLoopgroup
            /**
             * NioEventLoopGroup是用来  处理IO操作  以及  接收客户端连接  的事件循环组
             * 给定两个事件循环组
                 * 一个称之为boss,用来 接收连接
                 * 一个称之为worker，用来 处理已经接受的连接
             */
            boss = new NioEventLoopGroup(1);
            worker = new NioEventLoopGroup();//默认是 CPU个数乘以2    在Runtime.getRuntime().availableProcessors()用过

            /**
             * 启动辅助类：ServerBootStrap
             *  将配置信息配置在辅助类上，用来启动 或者 设置相关属性：TCP
             */
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    //指定事件循环组
                    .group(boss, worker)
                    //主（boss）事件循环组  接收的  通道的实例 类型
                    .channel(NioServerSocketChannel.class)//通过反射，创建一个实例
                    .childHandler(new ChannelInitializer<NioSocketChannel>() { //封装一个  子事件循环组 专门读取客户端数据的 业务逻辑
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            //存放channelHandler的容器
                            ChannelPipeline pipeline = ch.pipeline();
                            //字符串解码器
                            pipeline.addLast(new StringDecoder()); // 解码
                            pipeline.addLast(new StringEncoder());//  编码
                            //自定义channelHandler （处理业务逻辑）
                            pipeline.addLast(new ServerHandler());
                        }
                    });

            //通过 同步阻塞 方式 来 启动服务端(可以接受多个连接  接受多条消息，无需像NIO一样判断状态，直接通过相应方法触发)
            ChannelFuture channelFuture = serverBootstrap.bind(9999).sync();//调用sync会产生阻塞  直至 服务端绑定端口后 才返回
            System.out.println("服务端启动了");

            //使用 同步方法 来关闭程序（如通道实例  事件循环组）
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //关闭线程池资源
            boss.shutdownGracefully();//应先判断是否为null
            worker.shutdownGracefully();

        }

    }
}
