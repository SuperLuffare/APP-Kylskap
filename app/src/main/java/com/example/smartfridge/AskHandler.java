package com.example.smartfridge;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

public class AskHandler {
    private static String serverIP = "10.0.2.2";
    private static int port = 5100;
    private String securityKey;
    public static void setConnection(String ip, int newPort){
        serverIP = ip;
        port = newPort;

    }
    public AskHandler(){
        this.securityKey="kylskåp";
    }
    public void sendMessage(String message, Consumer<String> callback){
        new Thread(() -> {
            try {
                Socket socket = new Socket(serverIP, port);
                socket.setSoTimeout(5000);

                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.println(securityKey);
                out.println(message);

                String response = in.readLine();

                callback.accept(response);
            } catch(Exception e){
                callback.accept("Connection failed");
            }
        }).start();
    }
}
