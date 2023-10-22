import java.io.*;
import java.net.*;

import mensaje.DecoderEncoder;
import mensaje.Mensaje;

public class suma {
    final private static String HOST = "localhost"; // Host del middleware
    final private static int[] PORTS = {12345, 12346, 12347, 12348, 12349};
    private static DataOutputStream out;
    private static DataInputStream in;
    final private static String FILE= "server/logs.txt";
    private static int counter = 0;
    
    public static void main(String[] args) throws Exception{
        File file = new File(FILE);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        String tipoOperacion;
        String PORT;
        while((line = br.readLine()) != null){
            String[] array = line.split("\\,");
            tipoOperacion = array[0];
            PORT = array[1];
            System.out.println(tipoOperacion);
            System.out.println(PORT);
            if(tipoOperacion.equals("1")){
                System.out.println("The middleware with port{" + PORTS[counter] + "} is already occupied");
                counter += 1;
            }
        }
        try(Socket socket = new Socket(HOST, PORTS[counter])){
            System.out.println("The sum cell has successfully connected to the MOM with port: " + socket.getPort());
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            FileWriter writer = new FileWriter(file, true);
            writer.write("1," + socket.getPort() + "\n");
            writer.close();
            System.out.println("Log has been modified successfully");
            while(true){
                Mensaje mensaje = DecoderEncoder.leer(in);
                System.out.println("The message: " + mensaje + " has been received from: " + socket);
                
                
            }
        }
        catch(IOException e){
            System.err.println("There has been an error with the socket " + e.getMessage());
        }

    }

    

}

