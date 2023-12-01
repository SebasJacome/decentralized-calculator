package protocolosComunicacion;

import java.io.*;



public class MensajeOperacion implements Serializable, MensajeBase{
    private short tipo_operacion;
    private String evento;
    private float numero1;
    private float numero2;
    public String transmitterHashIdentifier = "";


    public MensajeOperacion(short operacion, String evento, float numero1, float numero2, String transmitterHashIdentifier){
        this.tipo_operacion = operacion;
        if(evento == ""){
            this.evento = getAlphaNumericString();
        }
        else{
            this.evento = evento;
        }
        this.numero1 = numero1;
        this.numero2 = numero2;
        this.transmitterHashIdentifier = transmitterHashIdentifier;
    }

    public void serializar(DataOutputStream dos) throws IOException{

        dos.writeShort(tipo_operacion);
        byte[] tam = evento.getBytes();
        short tamano_evento = (short)tam.length;
        short tamano_servicio = (short)(tamano_evento + 2 + 4 + 4);
        dos.writeShort(tamano_servicio);
        dos.writeShort(tamano_evento);
        dos.write(tam);
        dos.writeUTF(transmitterHashIdentifier);
        dos.writeFloat(numero1);
        dos.writeFloat(numero2);
        dos.flush();
    }

    public String getEvento() {
        return evento;
    }public float getNumero1() {
        return numero1;
    }public float getNumero2() {
        return numero2;
    }public short getTipoOperacion() {
        return tipo_operacion;
    }
    public String getTransmitterHashIdentifier() {
        return transmitterHashIdentifier;
    }

    @Override
    public String toString(){
        return "Mensaje{" + "tipoOperacion = " + tipo_operacion + ";ID = " + evento + ";num1 = " + numero1 + ";num2 = " + numero2 + "}" + ";Origin = " + getTransmitterHashIdentifier();
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
