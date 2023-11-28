package protocolosComunicacion;
import java.io.*;


public class Mensaje{

    DataInputStream dis;
    short tipoOperacion;
    String evento;
    public MensajeBase deserializarGeneral(DataInputStream dis) throws IOException{
        this.dis = dis;
        short tipoOperacion = dis.readShort();
        this.tipoOperacion = tipoOperacion;
        short tamanoServicio = dis.readShort();
        short tamanoEvento = dis.readShort();
        byte[] tam = new byte[(int)tamanoEvento];
        
        dis.read(tam);
        String evento = new String(tam);
        this.evento = evento;
        if(tipoOperacion == 99){
            System.out.println("Recibi un acuse");
            return mensajeAcuse(tipoOperacion, evento);
        }
        else if(tipoOperacion == 5){
            System.out.println("Recibi un resultado");
            return mensajeResultado(tipoOperacion, evento);
        }
        else{
            System.out.println("Recibi una operacion");
            return mensajeOperacion(tipoOperacion, evento);
        }
    }
    private MensajeAcuse mensajeAcuse(short tipo_operacion, String evento){
        return new MensajeAcuse(tipo_operacion, evento);
    }

    private MensajeOperacion mensajeOperacion(short tipo_operacion, String evento) throws IOException{
        float numero1 = dis.readFloat();
        float numero2 = dis.readFloat();
        return new MensajeOperacion(tipo_operacion, evento, numero1, numero2);
    }

    private MensajeResultado mensajeResultado(short tipo_operacion, String evento) throws IOException{
        float resultado = dis.readFloat();
        return new MensajeResultado(tipo_operacion, evento, resultado);
    }

    public String getEvento() {
        return evento;
    }
    public short getTipoOperacion() {
        return tipoOperacion;
    }
}