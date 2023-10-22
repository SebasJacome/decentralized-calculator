
import java.security.NoSuchAlgorithmException;
import java.io.*;





public class prueba {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException{
        System.out.println("String aleatorio: " + getAlphaNumericString());
        System.out.println("String aleatorio: " + getAlphaNumericString());
        System.out.println("String aleatorio: " + getAlphaNumericString());
    }

    public static String getAlphaNumericString() 
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
