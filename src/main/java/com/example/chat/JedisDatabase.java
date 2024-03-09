package com.example.chat;

import redis.clients.jedis.Jedis;

public class JedisDatabase {
    private static final Jedis instance = null;
    private JedisDatabase() {
    }
    public static Jedis getInstace() {
        if (instance == null) {
            return new Jedis("redis://default:WxO4dpKmCtVuvJpQIr6HWWNvdEoBt5H1@redis-17632.c302.asia-northeast1-1.gce.cloud.redislabs.com:17632");
        } else {
            return instance;
        }
    }


    public static void main(String[] args) {
        Jedis jedis = JedisDatabase.getInstace();
    }
}