package middleware;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


public class middleware {
    private static Socket socketMiddleware;
    final static String HOST = "localhost";
    final static int PORT_INTERFACE = 12345; // Port from which I'm listening
    final static int[] PORTS = {12345, 12346, 12347, 12348, 12349};
    private static List<Socket> clientSockets = new ArrayList<>();
    private static List<Socket> momSockets = new ArrayList<>();
    private static boolean isAnyConnected = false;

    public static void main(String[] args) {
        int counter = 0;
    
        while(true){
            try (ServerSocket serverSocket = new ServerSocket(PORTS[counter])) {
                System.out.println("Server is running and listening on port " + serverSocket.getLocalPort());
                if(PORTS[counter] == PORTS[0]){
                    isAnyConnected = false;
                }
                else{
                    isAnyConnected = true;
                    for(int i = 0; i<counter; i++){
                        try{
                            System.out.println("Connected to middleware with port: " + PORTS[i]);
                            socketMiddleware = new Socket(HOST, PORTS[i]);
                            clientSockets.add(socketMiddleware);

                            new ClientHandler(socketMiddleware).start();
                        }
                        catch(IOException e){
                            System.out.println("Error: " + e.getMessage());
                        }
                    }
                }
                while (true) {

                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket.getPort());
                    clientSockets.add(clientSocket);

                    new ClientHandler(clientSocket).start();
                }
            } catch (IOException e) {
                System.out.println("The port " + PORTS[counter] + " is already occupied");
                if(counter<5){
                    counter++;
                }
                else{
                    System.err.println("Error because the program is out of ports " + e.getMessage());
                }
            }
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
                InputStream in = this.socket.getInputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    System.out.println("Received: " + bytesRead + " from " + socket.getPort());
    
                    // Broadcast the message to all other clients
                    for (Socket clientSocket : clientSockets) {
                        try{
                            if(clientSocket.getPort() == socket.getPort()){
                                continue;
                            }
                            OutputStream writer = clientSocket.getOutputStream();
                            writer.write(buffer, 0, bytesRead);
                            writer.flush();
                            System.out.println("Sent to: " + clientSocket.getPort() + ": " + bytesRead);
                        } catch (IOException e) {
                            System.err.println("Error sending message to a client: " + e.getMessage());
                        }
                    }
                }
            } catch (IOException e) {
                if (!socket.isClosed()) {
                    System.err.println("Error handling client: " + e.getMessage());
                }
            } finally{
                clientSockets.remove(this.socket);
                System.out.println("Client disconnected: " + socket.getPort());
            }
        }
    }
    
}
