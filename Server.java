import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        int port = 4040;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server Started. Listening continuously on port " + port + "...");

            while (true) {
                System.out.println("\n---------------------------------");
                System.out.println("Waiting for a new connection...");
                
                try (Socket socket = serverSocket.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                    
                    System.out.println("A client connected from: " + socket.getRemoteSocketAddress());

                    String clientMessage = in.readLine();
                    System.out.println("[LOG] Client said: " + clientMessage);

                    out.println("Message received by server!");
                }

                System.out.println("Client disconnected. Looping back to listen again.");
            }
            
        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
        }
    }
}