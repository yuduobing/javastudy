//package com.example.javastudy.websocket;
//
///**
// * @author yuduobin[1510557673@qq.com]
// * @content
// */
//
//
//import com.chat.bean.ChatMsg;
//
//
//import com.chat.service.ChatMsgService;
//
//import com.chat.service.WorkForceService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import com.chat.reboo.api;
//import javax.websocket.*;
//import javax.websocket.server.PathParam;
//import javax.websocket.server.ServerEndpoint;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
// * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
// * @ServerEndpoint 可以把当前类变成websocket服务类
// */
//@Controller
//@ServerEndpoint(value = "/websocket/{userno}")
//public class ChatWebSocket {
//
////统计api
//
//    private static WorkForceService workForceService1;
//    // 这里使用静态，让 service 属于类
//    private static ChatMsgService chatMsgService;
//    // 注入的时候，给类的 service 注入
//    @Autowired
//    public void setChatService(ChatMsgService chatService) {
//        ChatWebSocket.chatMsgService = chatService;
//    }
//
//    @Autowired
//    public void setWorkForceService(WorkForceService workForceService1) {
//        ChatWebSocket.workForceService1 = workForceService1;
//    }
//
//    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
//    private static int onlineCount = 0;
//    private static int apitotal = 0;
//    private static int rapitotal = 0;
//    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
//
//    private static ConcurrentHashMap<String, ChatWebSocket> webSocketSet = new ConcurrentHashMap<String, ChatWebSocket>();
//    boolean a=true;
//
//    //与某个客户端的连接会话，需要通过它来给客户端发送数据
//    private Session WebSocketsession;
//    //当前发消息的人员编号
//    private String userno = "";
//
////通过线程调用
//
//    /**
//     * 连接建立成功调用的方法
//     *
//     * session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
//     */
//    @OnOpen
//    public void onOpen(@PathParam(value = "userno") String param, Session WebSocketsession, EndpointConfig config) {
//        userno = param;//接收到发送消息的人员编号
//        this.WebSocketsession = WebSocketsession;
//        webSocketSet.put(param, this);//加入map中
//        addOnlineCount();     //在线数加1
//        //System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
//    }
//
//
//    /**
//     * 连接关闭调用的方法
//     */
//    @OnClose
//    public void onClose() {
//        if (!userno.equals("")) {
//            webSocketSet.remove(userno); //从set中删除
//            subOnlineCount();     //在线数减1
//            //System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
//        }
//    }
//
//
//    /**
//     * 收到客户端消息后调用的方法
//     *
//     * @param message 客户端发送过来的消息
//     * @param session 可选的参数
//     */
//    @SuppressWarnings("unused")
//    @OnMessage
//    public void onMessage(String message, Session session)  {
//        //给指定的人发消息
//        System.out.println(message);
//        sendToUser(message);
//    }
//
//
//    /**
//     * 给指定的人发送消息
//     *a
//     * @param message
//     */
//    public void sendToUser(String message)  {
//        try {
//            String reviceUserid = message.split("[|]")[0];
//            String sendMessage1 = message.split("[|]")[1];
//            System.out.println("这是发送的消息"+sendMessage1);
//            //进行判断是不是进入人公共服务
//
//            if(sendMessage1.equals("人工服务")){
//
//                a=true;
//
//                webSocketSet.get(userno).sendMessage("已进入人工服务");
//            }
//            if(a){
//
//                apitotal++;
//                Date date = new Date();
//                SimpleDateFormat simpletype = new SimpleDateFormat("yyyy-mm-dd");
//                String a= simpletype.format(date);
//
//
//                workForceService1.insertTime(apitotal, a,rapitotal);
////            System.out.println("调用人工接口"+apitotal+a);
//                api api1=new api();
//                String sendMessageapi=api1.send(sendMessage1);
//                webSocketSet.get(userno).sendMessage("0"+"|"+sendMessageapi);
//            }
//            //插入到数据库
//            else {
//
//                rapitotal++;
//                Date date = new Date();
//                SimpleDateFormat simpletype = new SimpleDateFormat("yyyy-mm-dd");
//                String a= simpletype.format(date);
//
//                workForceService1.insertTime(apitotal, a,rapitotal);
////
//                //判断是否已经上线
//                if (webSocketSet.get(reviceUserid) != null) {
//                    webSocketSet.get(reviceUserid).sendMessage(userno+"|"+sendMessage1);
//                }else{
//                    webSocketSet.get(userno).sendMessage("0"+"|"+"当前用户不在线,消息已留言");
//                }
//                ChatMsg ChatMsg1=new ChatMsg();
//                ChatMsg1.setMsgtype("0");
//                ChatMsg1.setReciveuserid(reviceUserid);
//                ChatMsg1.setSenduserid(userno);
//                ChatMsg1.setSendtext(sendMessage1);
//                chatMsgService.InsertChatMsg(ChatMsg1);
//
//            }}
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 给所有人发消息
//     *
//     * @param message
//     */
//    private void sendAll(String message) {
//        //第一个元素为按照/划分
//        String sendMessage = message.split("[|]")[0];
//        //遍历HashMap
//        for (String key : webSocketSet.keySet()) {
//            try {
//                //判断接收用户是否是当前发消息的用户
//                if (!userno.equals(key)) {
//                    webSocketSet.get(key).sendMessage(sendMessage);
//                    System.out.println("key = " + key);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//    /**
//     * 发生错误时调用
//     *
//     * @param session
//     * @param error
//     */
//    @OnError
//    public void onError(Session session, Throwable error) {
//        error.printStackTrace();
//    }
//
//
//    /**
//     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
//     *
//     * @param message
//     * @throws IOException
//     */
//    public void sendMessage(String message) throws IOException {
//        this.WebSocketsession.getBasicRemote().sendText(message);
//        //this.session.getAsyncRemote().sendText(message);
//    }
//
//
//    public static synchronized int getOnlineCount() {
//        return onlineCount;
//    }
//
//
//    public static synchronized void addOnlineCount() {
//        ChatWebSocket.onlineCount++;
//    }
//
//
//    public static synchronized void subOnlineCount() {
//        ChatWebSocket.onlineCount--;
//    }
//
//}
//
