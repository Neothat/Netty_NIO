package com.geekbrains.geek.chat.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

public class MainHandler extends SimpleChannelInboundHandler<String> {
    private static final List<Channel> channels = new ArrayList<>();
    private static int newClientIndex = 1;
    private String clientName;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент подключился" + ctx);
        channels.add(ctx.channel());
        clientName = "Клиент №" + newClientIndex++;
        broadcastMessage("SERVER", clientName + " Подключился в чат!");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println("Полученно сообщение" + s);
        if (s.startsWith("/")) {
            if (s.startsWith("/changename ")) {
                clientName = s.split("\\s", 2)[1];
            }
            return;
        }
        broadcastMessage(clientName, s);
    }

    public void broadcastMessage(String clientName, String message){
        String out = String.format("[%s]: %s\n", clientName, message);
        for (Channel c : channels) {
            c.writeAndFlush(out);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Клиент " + clientName + " отвалился");
        channels.remove(ctx.channel());
        broadcastMessage("SERVER", clientName + " Покинул чат!");
        ctx.close();
    }
}
