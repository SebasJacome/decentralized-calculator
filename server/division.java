import java.io.*;
import java.net.*;

import foliadoMensajes.FilaEntrada;
import protocolosComunicacion.*;

public class division {
    final private static String HOST = "localhost"; // Host del middleware
    final private static int[] PORTS = {12345, 12346, 12347, 12348, 12349};
    private static DataOutputStream out;
    private static DataInputStream in;
    final private static String FILE= "./server/logs.txt";
    private static int counter = 0;
    
    public static void main(String[] args) throws Exception{
        FilaEntrada filaEntrada = new FilaEntrada();
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
            if(tipoOperacion.equals("4")){
                System.out.println("The middleware with port{" + PORTS[counter] + "} is already occupied by another division cell");
                counter += 1;
            }
        }
        br.close();
        try(Socket socket = new Socket(HOST, PORTS[counter])){
            System.out.println("The division cell has successfully connected to the MOM with port: " + socket.getPort());
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            FileWriter writer = new FileWriter(file, true);
            writer.write("4," + socket.getPort() + "\n");
            writer.close();
            System.out.println("Log has been modified successfully");
            while(true){
                System.out.println("Ya listo, ahora mensaje");
                Mensaje mensajeHandler = new Mensaje();
                MensajeBase mensaje = mensajeHandler.deserializarGeneral(in);
                MensajeOperacion operacion;
                if(mensaje instanceof MensajeOperacion){
                    operacion = (MensajeOperacion) mensaje;
                    filaEntrada.addMessage(operacion);
                    mandarAcuse(operacion.getEvento());
                }
                else{
                    System.out.println("The message has been discarded becuase its not an operation");
                    continue;
                }
                System.out.println("The message: " + operacion + " has been received from: " + socket.getPort());
                if(operacion.getTipoOperacion() == 4){
                    MensajeResultado mensajeResultado = solution(operacion);
                    mensajeResultado.serializar(out);
                    filaEntrada.getMessage();
                }
                else{
                    System.out.println("Message has been discarded because of the operation required");
                }
                
            }
        }
        catch(IOException e){
            System.err.println("There has been an error with the socket " + e.getMessage());
        }

    }

    public static void mandarAcuse(String evento) throws IOException{
        MensajeAcuse mensajeAcuse = new MensajeAcuse((short)99, "", evento);
        mensajeAcuse.serializar(out);
        System.out.println("Un acuse con tipo: " + mensajeAcuse.getTipoOperacion() + " fue creado");
    }

    public static MensajeResultado solution(MensajeOperacion m){
        MensajeResultado resultado;
        
        
        float operand1 = m.getNumero1();
        float operand2 = m.getNumero2();
        float result = 0.0f;
        Short tipoOperacion = 5;

        result = operand1 / operand2;
        resultado = new MensajeResultado((short)tipoOperacion, "", result, m.getEvento());
        System.out.println("Origin: " + m.getEvento());
        System.out.println("El mensaje es originario de: " + resultado.getTransmitterHashIdentifier());

        return resultado;
    }

    

}

