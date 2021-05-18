package com.example.javastudy.websocket;

/**
 * @author yuduobin[1510557673@qq.com]
 * @content
 */

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Component 将类注入到容器
 * @ServerEndpoint 前端通过这个url进行访问通信 建立连接
 */
@ServerEndpoint("/websocket/alarm")
@Component
public class MyWebSocket {



    //存放websocket 的线程安全的无序的集合
    //当一个属性被声明成类属性，那么所有的对象，都共享一个值
    private  static  ConcurrentHashMap<String, MyWebSocket>  websocket=new ConcurrentHashMap<>();
    private Session session;

    /*
    一堆getset方法
     */
    public static ConcurrentHashMap<String, MyWebSocket> getWebsocket() {
        return websocket;
    }

    public static void setWebsocket( ConcurrentHashMap<String, MyWebSocket> websocket) {
        MyWebSocket.websocket = websocket;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam(value = "userId") String param, Session session) {
        this.session = session;
        websocket.put(param,this);     //加入set中
        // addOnlineCount();           //在线数加1
        System.out.println("进入onOpen方法");
        try {
            sendMessage("连接已建立成功.");
        } catch (Exception e) {
            System.out.println("IO异常");
        }
    }

    /**
     * 关闭通信连接
     *
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
        //关闭连接后将此socket删除
        websocket.remove(this);
        System.out.println("进入onClose方法");
    }

    /**
     * 获取客户端发来的信息
     */
    @OnMessage
    public void onMessage(String message) throws IOException {
        String reviceUserid = message.split("[|]")[0];
        String sendMessage1 = message.split("[|]")[1];
        websocket.get(reviceUserid).sendMessage(sendMessage1);
        System.out.println("进入onMessage方法; message = " + message);
    }

    /**
     * 给客户端推送信息
     */
    public void sendMessage(String message) throws IOException {
        /*
        得到session发送给指定人
         getAsyncRemote()和getBasicRemote()确实是异步与同步

         和getBasicRemote最好一次发送完消息
         */
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 异常方法
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("进入error方法");
        error.printStackTrace();
    }
}
