package protocolosComunicacion;

import java.io.*;



public class MensajeResultado implements Serializable, MensajeBase{
    private short tipo_operacion;
    private String evento;
    private float resultado;
    public String transmitterHashIdentifier = "";


    public MensajeResultado(short operacion, String evento, float resultado, String transmitterHashIdentifier){
        this.tipo_operacion = operacion;
        if(evento == ""){
            this.evento = getAlphaNumericString();
        }
        else{
            this.evento = evento;
        }
        this.resultado = resultado;
        this.transmitterHashIdentifier = transmitterHashIdentifier;
    }

    public void serializar(DataOutputStream dos) throws IOException{

        dos.writeShort(tipo_operacion);
        byte[] tam = evento.getBytes();
        short tamano_evento = (short)tam.length;
        short tamano_servicio = (short)(tamano_evento + 2 + 4);
        dos.writeShort(tamano_servicio);
        dos.writeShort(tamano_evento);
        dos.write(tam);
        dos.writeUTF(transmitterHashIdentifier);
        dos.writeFloat(resultado);
    }

    public static MensajeResultado deserializar(DataInputStream dis) throws IOException{

        short tipoOperacion = dis.readShort();
        short tamanoServicio = dis.readShort();
        short tamanoEvento = dis.readShort();
        byte[] tam = new byte[(int)tamanoEvento];
        dis.read(tam);
        String evento = new String(tam);
        String origin = dis.readUTF();
        float resultado = dis.readFloat();

        return new MensajeResultado(tipoOperacion, evento, resultado, origin);
    }

    public String getEvento() {
        return evento;
    }public float getResultado() {
        return resultado;
    }public short getTipoOperacion() {
        return tipo_operacion;
    }
    public void setTipo_operacion(short tipo_operacion) {
        this.tipo_operacion = tipo_operacion;
    }
    public void setEvento(String evento) {
        this.evento = evento;
    }
    public short getTipo_operacion() {
        return tipo_operacion;
    }
    public void setResultado(float resultado){
        this.resultado = resultado;
    }
    public String getTransmitterHashIdentifier() {
        return transmitterHashIdentifier;
    }

    @Override
    public String toString(){
        return "Mensaje{" + "tipoOperacion = " + tipo_operacion + ";ID = " + evento + ";res = " + resultado + "}" + ";Origin = " + getTransmitterHashIdentifier();
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
