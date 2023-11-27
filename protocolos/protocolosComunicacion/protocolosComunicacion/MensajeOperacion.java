package protocolosComunicacion;

import java.io.*;



public class MensajeOperacion implements Serializable{
    private short tipo_operacion;
    private String evento;
    private int numero1;
    private int numero2;


    public MensajeOperacion(short operacion, String evento, int numero1, int numero2){
        this.tipo_operacion = operacion;
        if(evento == ""){
            this.evento = getAlphaNumericString();
        }
        else{
            this.evento = evento;
        }
        this.numero1 = numero1;
        this.numero2 = numero2;
    }

    public void serializar(DataOutputStream dos) throws IOException{

        dos.writeShort(tipo_operacion);
        byte[] tam = evento.getBytes();
        short tamano_evento = (short)tam.length;
        short tamano_servicio = (short)(tamano_evento + 2 + 4 + 4);
        dos.writeShort(tamano_servicio);
        dos.writeShort(tamano_evento);
        dos.write(tam);
        dos.writeInt(numero1);
        dos.writeInt(numero2);
    }

    public static MensajeOperacion deserializar(DataInputStream dis) throws IOException{

        short tipoOperacion = dis.readShort();
        short tamanoServicio = dis.readShort();
        short tamanoEvento = dis.readShort();
        byte[] tam = new byte[(int)tamanoEvento];
        dis.read(tam);
        String evento = new String(tam);
        int numero1 = dis.readInt();
        int numero2 = dis.readInt();
        return new MensajeOperacion(tipoOperacion, evento, numero1, numero2);
    }

    public String getEvento() {
        return evento;
    }public int getNumero1() {
        return numero1;
    }public int getNumero2() {
        return numero2;
    }public short getTipo_operacion() {
        return tipo_operacion;
    }

    @Override
    public String toString(){
        return "Mensaje{" + "tipoOperacion = " + tipo_operacion + ";ID = " + evento + ";num1 = " + numero1 + ";num2 = " + numero2 + "}";
    }
    
    private String getAlphaNumericString() 
    { 
        int n = 20;
        // choose a Character random from this String 
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz"; 
        
        // create StringBuffer size of AlphaNumericString 
        StringBuilder sb = new StringBuilder(n); 
        
        for (int i = 0; i < n; i++) { 
        
        // generate a random number between 
        // 0 to AlphaNumericString variable length 
        int index 
            = (int)(AlphaNumericString.length() 
            * Math.random()); 
        
        // add Character one by one in end of sb 
        sb.append(AlphaNumericString 
            .charAt(index)); 
        } 
        
        return sb.toString(); 
    } 
}
