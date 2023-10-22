
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;





public class prueba {
    final private static String PATH_MOM_LOG = "./middleware/middleware/logs.txt";
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException{
        String str = "3,3";
        double result = 6.0;
        str = str + "," + result;
        System.out.println(str.getBytes());
        String newStr = new String(str.getBytes());
        System.out.println(newStr);
    }
}
