import java.io.*;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 4040;

        System.out.println("Attempting to connect to the server...");

        try (Socket socket = new Socket(hostname, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Connected to server successfully!");

            out.println("I'm learnings about sockets!");

            String serverResponse = in.readLine();
            System.out.println("Server response: " + serverResponse);

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}
