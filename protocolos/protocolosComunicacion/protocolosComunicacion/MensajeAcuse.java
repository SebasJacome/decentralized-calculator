package protocolosComunicacion;

import java.io.*;



public class MensajeAcuse implements Serializable, MensajeBase{
    private short tipo_operacion;
    private String evento;
    public String transmitterHashIdentifier = "";

    public MensajeAcuse(short operacion, String evento, String transmitterHashIdentifier){
        this.tipo_operacion = operacion;
        if(evento == ""){
            this.evento = getAlphaNumericString();
        }
        else{
            this.evento = evento;
        }
        this.transmitterHashIdentifier = transmitterHashIdentifier;

    }

    public void serializar(DataOutputStream dos) throws IOException{

        dos.writeShort(tipo_operacion);
        byte[] tam = evento.getBytes();
        short tamano_evento = (short)tam.length;
        short tamano_servicio = (short)(tamano_evento + 2);
        dos.writeShort(tamano_servicio);
        dos.writeShort(tamano_evento);
        dos.write(tam);
        dos.writeUTF(transmitterHashIdentifier);

    }

    public String getEvento() {
        return evento;
    }public short getTipoOperacion() {
        return tipo_operacion;
    }
    public String getTransmitterHashIdentifier() {
        return transmitterHashIdentifier;
    }

    @Override
    public String toString(){
        return "Mensaje{" + "tipoOperacion = " + tipo_operacion + ";ID = " + evento  + "}" + ";Origin = " + getTransmitterHashIdentifier();
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
