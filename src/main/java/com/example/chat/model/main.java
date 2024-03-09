package com.example.chat.model;

public class main {
    public static void main(String[] args) {
        Mess.ChatMessage message = Mess.ChatMessage.newBuilder()
                .setDate("23/3")
                .setMess("tao la hai ne")
                .setName("Hai").build();

        for (int i = 0; i <message.toByteString().size() ; i++) {
            System.out.println(message.toByteString().substring(i,i+1));
        }



    }
}
