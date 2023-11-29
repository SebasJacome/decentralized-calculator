package protocolosComunicacion;
import java.io.*;


public class Mensaje{

    DataInputStream dis;
    short tipoOperacion;
    String evento;
    String origin;
    public MensajeBase deserializarGeneral(DataInputStream dis) throws IOException{
        this.dis = dis;
        short tipoOperacion = dis.readShort();
        this.tipoOperacion = tipoOperacion;
        short tamanoServicio = dis.readShort();
        short tamanoEvento = dis.readShort();
        byte[] tam = new byte[(int)tamanoEvento];
        dis.read(tam);
        String origin = dis.readUTF();
        String evento = new String(tam);
        this.evento = evento;
        this.origin = origin;
        System.out.println("Mensaje de tipo: " + tipoOperacion);
        if(tipoOperacion == 99){
            System.out.println("Recibi un acuse");
            return mensajeAcuse(tipoOperacion, evento, origin);
        }
        else if(tipoOperacion == 5){
            System.out.println("Recibi un resultado");
            return mensajeResultado(tipoOperacion, evento, origin);
        }
        else{
            System.out.println("Recibi una operacion prueba");
            return mensajeOperacion(tipoOperacion, evento, origin);
        }
    }
    private MensajeAcuse mensajeAcuse(short tipo_operacion, String evento, String origin){
        return new MensajeAcuse(tipo_operacion, evento, origin);
    }

    private MensajeOperacion mensajeOperacion(short tipo_operacion, String evento, String origin) throws IOException{
        float numero1 = dis.readFloat();
        float numero2 = dis.readFloat();
        return new MensajeOperacion(tipo_operacion, evento, numero1, numero2, origin);
    }

    private MensajeResultado mensajeResultado(short tipo_operacion, String evento, String origin) throws IOException{
        float resultado = dis.readFloat();
        return new MensajeResultado(tipo_operacion, evento, resultado, origin);
    }

    public String getEvento() {
        return evento;
    }
    public short getTipoOperacion() {
        return tipoOperacion;
    }
}