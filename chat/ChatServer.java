package chat;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private static final int PORT = 4040;
    // Keep track of all connected users' output streams to broadcast messages
    private static List<PrintWriter> clientWriters = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Chat Server started on port " + PORT + "...");
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("A new user connected!");

                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                synchronized (clientWriters) {
                    clientWriters.add(writer);
                }

                // Start a new thread dedicated to handling this specific user
                new Thread(new ClientHandler(socket, writer)).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    // Broadcasts a message to everyone else connected
    public static void broadcast(String message, PrintWriter senderWriter) {
        synchronized (clientWriters) {
            for (PrintWriter writer : clientWriters) {
                // Don't send the message back to the person who wrote it
                if (writer != senderWriter) {
                    writer.println(message);
                }
            }
        }
    }

    // Inner class to handle background communication for a single client
    private static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter writer;

        public ClientHandler(Socket socket, PrintWriter writer) {
            this.socket = socket;
            this.writer = writer;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String message;
                // Continuously listen for messages from this client
                while ((message = in.readLine()) != null) {
                    System.out.println("Received: " + message);
                    broadcast(message, writer);
                }
            } catch (IOException e) {
                System.out.println("A user disconnected.");
            } finally {
                synchronized (clientWriters) {
                    clientWriters.remove(writer);
                }
                try { socket.close(); } catch (IOException e) { }
            }
        }
    }
}