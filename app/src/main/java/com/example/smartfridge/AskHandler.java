package com.example.smartfridge;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

public class AskHandler {
    private static String serverIP = "10.0.2.2";
    private static int port = 5100;
    private String securityKey = "kylskåp";

    /**
     * Ny ip och port. Användaren skriver in
     * @param ip
     * @param newPort
     */
    public static void setConnection(String ip, int newPort) {
        serverIP = ip;
        port = newPort;

    }

    /**
     * Skickar meddelande till servern
     * @param message
     * @param callback
     * @author Jakob
     */
    public void sendMessage(String message, Consumer<String> callback) {
        new Thread(() -> {
            try (Socket socket = new Socket(serverIP, port);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                socket.setSoTimeout(5000);

                out.println(securityKey);
                out.println(message);

                String response = in.readLine();

                if (response == null) {
                    callback.accept("ERROR: EMPTY_RESPONSE");
                } else {
                    callback.accept(response);
                }

            } catch (Exception e) {
                callback.accept("ERROR: CONNECTION_FAILED");
            }
        }).start();
    }
}
