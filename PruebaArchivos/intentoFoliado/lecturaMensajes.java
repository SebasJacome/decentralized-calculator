package intentoFoliado;
import protocolosComunicacion.*;
import java.util.Scanner;
import java.io.*;
import java.net.*;
public class lecturaMensajes {
    

    public static void main(String[] args) throws IOException{
        MensajeAcuse msj;
        Socket socket = new Socket("localhost", 9876);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        msj = new MensajeAcuse((short)99, "ben");
        msj.serializar(dos);
        System.out.println("El mensaje fue serializado correctamente");
        socket.close();
    }
}
