package mensaje;

public class Mensaje{
    
    Short tipoOperacion;
    byte[] datos;
    String hashIdentifier;
    
    public Mensaje(){
        this.hashIdentifier = getAlphaNumericString();
    }

    public Short getTipoOperacion(){
        return tipoOperacion;
    }

    public void setTipoOperacion(Short tipoOperacion){
        this.tipoOperacion = tipoOperacion;
    }

    public byte[] getDatos(){
        return datos;
    }

    public void setDatos(byte[] datos){
        this.datos = datos;
    }

    @Override
    public String toString(){
        return "Mensaje{" + "tipoOperacion = " + tipoOperacion + ";ID = " + hashIdentifier +"}";
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