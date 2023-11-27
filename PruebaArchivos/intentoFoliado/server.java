package intentoFoliado;
import java.io.*;
import java.net.*;
import protocolosComunicacion.MensajeOperacion;
public class server {
    public static void main(String[] args) throws IOException{
        ServerSocket server = new ServerSocket(9876);
        while(true){
            System.out.println("Waiting for the client request");
            Socket socket = server.accept();
            System.out.println("Client: " + socket.getLocalPort() + " has connected");
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            MensajeOperacion mensaje = MensajeOperacion.deserializar(dis);
            System.out.println("Se imprimó correctamente");
        }
    }
}
