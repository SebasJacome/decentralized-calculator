package mensaje;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DecoderEncoder {

    public static Mensaje leer(DataInputStream dis) throws IOException {
        // ciclo de lectura
        // se lee el tipo de operacion
        Short tipoOperacion = dis.readShort();
        // se lee el tam del arreglo
        Short tam = dis.readShort();
        // leer el arreglo de datos
        byte[] datos = new byte[tam];
        dis.readFully(datos);
        // leer el identificador hash
        String hashIdentifier = dis.readUTF();
        String transmitterHashIdentifier = dis.readUTF();
        Mensaje m = new Mensaje(false);
        m.setTipoOperacion(tipoOperacion);
        m.setDatos(datos);
        m.hashIdentifier = hashIdentifier;
        m.setTransmitterHashIdentifier(transmitterHashIdentifier);
        return m;
    }

    public static void escribir(DataOutputStream dos, Mensaje mensaje) throws IOException {
        // tam del arreglo
        Short tam = (short) mensaje.getDatos().length;
        // enviar el tipo de operacion
        dos.writeShort(mensaje.getTipoOperacion());
        // enviar el tam del mensaje
        dos.writeShort(tam);
        // enviar el mensaje en bytes
        dos.write(mensaje.getDatos());
        dos.writeUTF(mensaje.hashIdentifier);
        dos.writeUTF(mensaje.transmitterHashIdentifier);

    }

}
