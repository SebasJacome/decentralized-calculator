package middleware;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class middleware {
    final static String HOST = "localhost";
    final static int PORT_INTERFACE = 12345; // Port from which I'm listening
    private static List<Socket> clientSockets = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT_INTERFACE)) {
            System.out.println("Server is running and listening on port " + PORT_INTERFACE);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getPort());
                clientSockets.add(clientSocket);

                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static class ClientHandler extends Thread {
        private final Socket socket;
    
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }
    
        @Override
        public void run() {
            try{
                BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                String clientMessage;
    
                while ((clientMessage = in.readLine()) != null) {
                    System.out.println("Received: " + clientMessage + " from " + socket.getPort());
    
                    // Broadcast the message to all other clients
                    for (Socket clientSocket : clientSockets) {
                        try{
                            if(clientSocket.getPort() == socket.getPort()){
                                continue;
                            }
                            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                            writer.println(clientMessage);
                            System.out.println("Sent to: " + clientSocket.getPort() + ": " + clientMessage);
                        } catch (IOException e) {
                            System.err.println("Error sending message to a client: " + e.getMessage());
                        }
                    }
                }
            } catch (IOException e) {
                if (!socket.isClosed()) {
                    System.err.println("Error handling client: " + e.getMessage());
                }
            } 
        }
    }
    
}
