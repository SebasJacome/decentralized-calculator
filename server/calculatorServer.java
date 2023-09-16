import java.io.*;
import java.net.*;
import java.lang.Math;
import java.util.HashMap;

public class calculatorServer {

    private static Socket socketMiddleware;
    private static OutputStream out;
    private static InputStream in;
    public static void main(String[] args) {
        final String HOST = "localhost"; // Host del middleware
        final int PORT = 12345; // Puerto del middleware

        try {
            System.out.println("Server is running");
            socketMiddleware = new Socket(HOST, PORT);
            out = socketMiddleware.getOutputStream();
            in = socketMiddleware.getInputStream();
       } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        while(true){
            try{
                System.out.println("Waiting for operation");
                byte[] buffer = new byte[1024];
                int bytesRead = in.read(buffer);

                

                byte[] receivedData = new byte[bytesRead];
                System.arraycopy(buffer, 0, receivedData, 0, bytesRead);
                System.out.println("Received data: " + new String(receivedData, 0, bytesRead));

                if (receivedData.length == 0) {
                    System.out.println("Message discarded");
                    continue;
                } else {
                    String result = decodeOperation(new String(receivedData, 0, bytesRead));
                    System.out.println("This is decoded operation: " + result);
                    out.write(result.getBytes());
                    out.flush();
                }
                
            }catch(IOException e){
                System.out.println("Error: " + e.getMessage());
            }
            

        }

        
        
    }

    public static String decodeOperation(final String str) {
        String string = str;
        String[] stringArray = string.split("\\|");
        int operator = Integer.parseInt(stringArray[0]);
        String[] operands = stringArray[1].split(",");
        double operand1 = Double.parseDouble(operands[0]);
        double operand2 = Double.parseDouble(operands[1]);
        double result = 0.0;
        switch(operator){
            case 1:
                result = operand1 + operand2;
                break;
            case 2:
                result = operand1 - operand2;
                break;
            case 3:
                result = operand1 * operand2;
                break;
            case 4: 
                result = operand1 / operand2;
                break;
            case 100:
                System.out.println("Message discarded: ");
                return "";
            default:
                break;
        }


        return "100" + "|" + operand1 + "," + operand2 + "," + result;


        
    }
}

