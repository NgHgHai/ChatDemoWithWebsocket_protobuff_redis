package com.example.chat.endpoint;

import com.example.chat.JedisDatabase;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import redis.clients.jedis.Jedis;

import java.io.IOException;

@ServerEndpoint("/host-room")
public class HostRoomEndpoint {
    Jedis jedis = JedisDatabase.getInstace();
    @OnOpen
    public void CreateRoom(Session session) throws IOException {
        // Khi host can phai xac thuc nguoi host rang la 1 nguoi dung cua he thong
        // Khi host tao ra 1 room, he thong se tao ra 1 room id va gui ve cho host
        // Khi host nhan duoc room id, host se tu vao phong thong qua id do
        int ssid = (int) (Math.random() * 1000000);
        //check first if the room already exists
        while (jedis.sismember("rooms", "room#"+String.valueOf(ssid))) {
            ssid = (int) (Math.random() * 1000000);
        }
        //create the room and add it to the set named "rooms"
        jedis.sadd("rooms", "room_"+String.valueOf(ssid));
        //send the room id to the client to join the room as a host
        session.getBasicRemote().sendText("room_"+String.valueOf(ssid));
        session.close();

    }
    @OnMessage
    public String echo(String message) {
        return "echo: " + message;
    }
}
