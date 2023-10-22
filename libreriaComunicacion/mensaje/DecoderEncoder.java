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
        // se imprime en pantalla
        String mensaje = new String(datos);

        Mensaje m = new Mensaje();
        m.setTipoOperacion(tipoOperacion);
        m.setDatos(datos);

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

    }

}
