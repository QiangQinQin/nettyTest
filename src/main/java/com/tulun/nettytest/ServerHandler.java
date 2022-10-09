package com.tulun.nettytest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
/*
NIO编程
    每走一步，要考虑他下一步干什么
    （比如先创建ServerSocketChannel,然后注册到selector,阻塞直到接受事件，判断是可读？ 可接受事件？然后分别处理
      即除了考虑业务逻辑，还要考虑各个状态的问题）

Netty将各个状态 封装成了不同的方法，就不用关注是不是 可接受、连接成功、下线状态等
     直接在各个方法上内 写好业务逻辑
* */
/**
 *  自定义channelHandler
 *  读取数据的 接口：ChannelInboundHandler
 *  抽象类SimpleChannelInboundHandler  对 ChannelInboundHandler里面的所有方法都有一个默认的实现
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> {
    //接收数据方法
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("channelRead0："+msg);
    }

    //接收数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //展示 （ 利用  ctx.channel().    可以得到数据信息  以及  连接的基本信息）
        System.out.println(ctx.channel().remoteAddress()+":"+msg);

        //将数据回写给客户端   （其实收到 echo字符串 后，可将其解析成命令，得到执行结果）
        ctx.channel().writeAndFlush("echo:"+msg);

        //测试unpooled
//        ByteBuf buffer = Unpooled.buffer(10);
//        buffer.readByte();
//        buffer.writeByte(23);

    }

    //连接成功触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+" 连接服务端成功");
    }

    //断开连接触发给方法
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+" 断开和服务端连接");
    }

   // 还有更多方法，输入pub    会自动提示
}