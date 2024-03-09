package com.example.chat;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;

@jakarta.websocket.server.ServerEndpoint("/echo")
public class ServerEndpoint {
    static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    @OnOpen
    public void onOpen(Session session) {
        System.out.println(format("%s joined the chat room.", session.getId()));
        peers.add(session);
    }
//    @OnMessage
//    public void onMessage(String message, Session session) throws IOException {
//        System.out.println(format("%s: %s", session.getId(), message));
//        for (Session peer : peers) {
//            if (!peer.equals(session)) {
//                peer.getBasicRemote().sendText(format("%s: %s", session.getId(), message));
//            }
//        }
//    }
    @OnClose
    public void onClose(Session session) {
        System.out.println(format("%s left the chat room.", session.getId()));
        peers.remove(session);
        for (Session peer : peers) {
            try {
                peer.getBasicRemote().sendText(format("%s left the chat room.", session.getId()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @OnMessage
    public String echo(String message) {
       return "echo: " + message;
    }

}
