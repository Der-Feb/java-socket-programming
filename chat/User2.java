package chat;

import java.io.*;
import java.net.Socket;

public class User2 {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 4040;
        String username = "User 2";

        // Moving Socket inside the try() fixes the resource leak warning!
        try (Socket socket = new Socket(hostname, port)) {
            System.out.println("Connected to the chat! Type your message and press Enter.");

            new Thread(() -> {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String incomingMessage;
                    while ((incomingMessage = in.readLine()) != null) {
                        System.out.println("\n" + incomingMessage);
                        System.out.print(username + ": ");
                    }
                } catch (IOException e) {
                    System.out.println("\nDisconnected from server.");
                }
            }).start();

            try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
                
                String userInput;
                System.out.print(username + ": ");
                while ((userInput = console.readLine()) != null) {
                    out.println(username + ": " + userInput);
                    System.out.print(username + ": ");
                }
            }

        } catch (IOException e) {
            System.err.println("Could not connect to chat server: " + e.getMessage());
        }
    }
}
