package com.example.chat.endpoint;

import com.example.chat.JedisDatabase;
import com.example.chat.model.Mess;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/chat/{roomId}")
public class ChatEndpoint {
    static Set<Session>  peers = Collections.synchronizedSet(new HashSet<Session>());
    Jedis jedis = JedisDatabase.getInstace();

    @OnOpen
    public void join(Session session, @PathParam("roomId") String roomId) throws IOException {
        System.out.println("Open Connection ...");
        peers.add(session);

        session.getUserProperties().put("roomId", roomId);

        if (jedis.exists(roomId)) {
            jedis.sadd(roomId, "client#" + session.getId());
        } else {
            jedis.sadd(roomId, "host#" + session.getId());
        }
        session.getAsyncRemote().sendText("You are now connected to room: " + roomId+"id: "+session.getId());

        notifyAllPeers(roomId,"New user joined: " + session.getId());

    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("Message from " + session.getId() + ": " + message);
        Mess.ChatMessage mess = Mess.ChatMessage.newBuilder()
                .setMess("tao la hoang hai")
                .setDate("23/3")
                .setName("Hai")
                .build();
        notifyAllPeers((String) session.getUserProperties().get("roomId"),session.getId() + ": " + message);
        if (message.equals("all")) {
            session.getBasicRemote().sendText("All users: " + peers.size());
            }
        }

    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println("Close Connection ...");
        jedis.srem((String) session.getUserProperties().get("roomId"), "client#" + session.getId());
        if (jedis.sismember((String) session.getUserProperties().get("roomId"), "host#" + session.getId())) {
            jedis.del((String) session.getUserProperties().get("roomId"));
            for (Session peer : peers) {
                if (peer.getId().equals(session.getId())) {
                    peer.close();
                }
            }
        }
        peers.remove(session);
        notifyAllPeers((String) session.getUserProperties().get("roomId"),"User disconnected: " + session.getId());
    }

    public void notifyAllPeers(String roomId, String message) throws IOException {
        Set<String> clients = jedis.smembers(roomId);
        for (Session peer : peers) {
            if (clients.contains("client#" + peer.getId()) || clients.contains("host#" + peer.getId())) {
                peer.getBasicRemote().sendText(message);
            }
        }
    }
}
