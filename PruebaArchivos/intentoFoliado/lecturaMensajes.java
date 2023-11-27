package intentoFoliado;
import protocolosComunicacion.MensajeOperacion;
import java.io.*;
import java.net.*;
public class lecturaMensajes {
    

    public static void main(String[] args) throws IOException{
        MensajeOperacion msj = new MensajeOperacion((short) 1, "", 2, 3);

        Socket socket = new Socket("localhost", 9876);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        msj.serializar(dos);
        System.out.println("El mensaje fue serializado");
        socket.close();
    }
}
