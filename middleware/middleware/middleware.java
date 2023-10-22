package middleware;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import mensaje.Mensaje;
import mensaje.DecoderEncoder;

public class middleware {
    private static Socket socketMiddleware;
    final static String HOST = "localhost";
    final static int[] PORTS = {12345, 12346, 12347, 12348, 12349};
    private static List<Socket> clientSockets = new ArrayList<>();
    private static List<String> hashIdentifiers = new ArrayList<>();
    final private static String PATH_MOM_LOG = "./middleware/middleware/logs.txt";
    final private static String PATH_SERVER_LOG = "./server/logs.txt";

    public static void main(String[] args) {
        int counter = 0;
         
        while(true){
            try (ServerSocket serverSocket = new ServerSocket(PORTS[counter])) {
                System.out.println("Server is running and listening on port " + serverSocket.getLocalPort());
                
                if(PORTS[counter] != PORTS[0]){
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
                else{
                    File momLogs = new File(PATH_MOM_LOG);
                    File serverLogs = new File(PATH_SERVER_LOG); 
                    
                    while(true){
                        if(momLogs.createNewFile() && serverLogs.createNewFile()){
                            System.out.println("Files created");
                            break;
                        }
                        else{
                            System.out.println("Files already exist. Deleting them now...");
                            momLogs.delete(); serverLogs.delete();
                        }
                    }
                }
                FileWriter momLogsWriter = new FileWriter(PATH_MOM_LOG, true);
                momLogsWriter.write(serverSocket.getLocalPort() + "\n");
                momLogsWriter.close();
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
            try(DataInputStream in = new DataInputStream(this.socket.getInputStream());){
                while(true){
                    Mensaje mensaje = DecoderEncoder.leer(in);
                    System.out.println("Received: " + mensaje.getTipoOperacion() + "|" + mensaje.getDatosString() + ", from client: " + socket.getPort());
                    if(!hashIdentifiers.contains(mensaje.getHashIdentifier())){
                        hashIdentifiers.add(mensaje.getHashIdentifier());
                        for(Socket socketTemp : clientSockets){
                            System.out.println("Sent to: " + socketTemp.getPort() + ": " + mensaje.getDatosString());
                            DecoderEncoder.escribir(new DataOutputStream(socketTemp.getOutputStream()), mensaje);
                        } 
                    }
                    else{
                        System.out.println("This message will be discarded because I've got it before");
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
