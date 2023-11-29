package controller;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.Button;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;
import foliadoMensajes.FilasSalida;
import protocolosComunicacion.*;

public class MainController{

    @FXML
    private Label result;
    @FXML
    private Label result1;
    private boolean start = true;
    private boolean isOperator = false;
    private boolean isFloat = false;
    private String operator = "";

    private Socket socketMiddleware;
    private DataOutputStream out;
    private DataInputStream in;


    private MensajeOperacion msj;

    private ArrayList<String> sentMessages = new ArrayList<>();
    private FilasSalida filasSalida = new FilasSalida();
    private int maxAcknowledge = 3;

    

    final private static String PATH_MOM_LOG = "./middleware/middleware/logs.txt";

    public MainController() {
        // Initialize the socket connection in the constructor
        final String HOST = "localhost"; // Host del middleware
        int PORT = 12345; // Puerto del middleware default es el 12345
        ArrayList<Integer> PORTS = new ArrayList<Integer>();
        try{
            File logReader = new File(PATH_MOM_LOG);
            Scanner myReader = new Scanner(logReader);
            while(myReader.hasNextLine()){
                try{
                    PORTS.add(myReader.nextInt());
                }
                catch(Exception e){
                    break;
                }
            }
            myReader.close();
            int rand = (int) (Math.random() * PORTS.size());
            PORT = PORTS.get(rand);
            System.out.println("Calculator will connect to: " + HOST + "::" + PORT);
        }
        catch(FileNotFoundException e){
            System.err.println("An error ocurred while reading the file, port 12345 will be used by defaut " + e.getStackTrace());
        }
        
        
        try {
            socketMiddleware = new Socket(HOST, PORT);
            out = new DataOutputStream(socketMiddleware.getOutputStream());
            in = new DataInputStream(socketMiddleware.getInputStream());

            Thread responseThread = new Thread(this::receiveResponses);
            responseThread.setDaemon(true); // Make it a daemon thread so it exits when the application exits
            responseThread.start();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            // Handle the error gracefully in your application
        }
    }

    public void processNumbers(ActionEvent event){
        if(start){
            result.setText("");
            result1.setText("");
            start = false;
        }
        
        String value = ((Button)event.getSource()).getText();
        if(value.equals(".")){
            if(!isFloat){
                isFloat = true;
                result.setText(result.getText() + value);
            }
            return;
        }
        else{
            isFloat = false;
        }
        result.setText(result.getText() + value); 
        
    }

    public void processOperator(ActionEvent event) throws IOException{
        String value = ((Button)event.getSource()).getText();
        
        if(!value.equals("=")){
            if(!value.equals("AC") && !isOperator && result.getText().length() > 0){
                isOperator = true;
                operator = value;
                result.setText(result.getText() + value);
                
            }else if(value.equals("AC")){
                result.setText("0");
                result1.setText("");
                start = true;
                isOperator = false;
            }
        }
        else{
            float[] encodedOperands = encodeOperands(result.getText());

            msj = new MensajeOperacion(encodeOperator(operator), "", encodedOperands[0], encodedOperands[1], "");
            msj.transmitterHashIdentifier = msj.getEvento();
            this.sentMessages.add(msj.getEvento());
            System.out.println("Escribi un mensaje de operacion con este hash: " + msj.getEvento() + " y estos numeros: " + msj.getNumero1() + ", " + msj.getNumero2());
            isOperator = false;
            start = true;
            //Se mete a la cola el mensaje
            filasSalida.addMessage(msj, msj.getTipoOperacion());
            //Se mete el protocolo a esperar N acuses de recibido
            sendOperation(msj);
    
        }
    }

    public void sendOperation(MensajeOperacion m){
        try{
            m.serializar(out);
        }
        catch(IOException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void receiveResponses() {
        try {
            while (true) {
                Mensaje mensajeHandler = new Mensaje();
                MensajeBase mensaje;
                mensaje = mensajeHandler.deserializarGeneral(in);
                System.out.println("Recibí un mensaje de clase: " + mensaje.getClass() + " de tipo: " + mensaje.getTipoOperacion());
                String response;
                if(mensaje instanceof MensajeResultado){
                    MensajeResultado respuesta = (MensajeResultado) mensaje;
                    response = respuesta.getTipoOperacion() + "|" + respuesta.getResultado();
                    if (response != "") {
                        // Update the UI with the received response
                        if(sentMessages.contains(respuesta.getTransmitterHashIdentifier()))
                        {
                            updateUIWithResponse(response);
                        }
                        else{
                            System.out.println("Message will be discarded because it's not for me");
                        }
                    } else {
                        System.out.println("Server closed the connection.");
                        break;
                    }
                }
                else{
                    System.out.println("Recibi un mensaje, pero no es una respuesta");
                    continue;
                }
                
            }
        } catch (IOException e) {
            System.out.println("Error while receiving responses: " + e.getMessage());
        }
    }

    private void updateUIWithResponse(String response) {
        
        Platform.runLater(() -> {
            if(!response.equals("")){
                result1.setText(result1.getText() + "\n" + response);
                if(response.charAt(0) == '5'){
                    decodeResult(response);
                }
            }
        });
        
    }
    
    public Short encodeOperator(String operator){
        

        switch(operator){
            case "+":
                return 1;
            case "-":
                return 2;
            case "*":
                return 3;
            case "/": 
                return 4;
            default:
                return 0;
        }
    }


    public float[] encodeOperands(String operands){
        float[] encodedOperands = new float[2];
        String[] operandsArray = operands.split("\\+|\\-|\\*|\\/");
        encodedOperands[0] = Float.parseFloat(operandsArray[0]);
        encodedOperands[1] = Float.parseFloat(operandsArray[1]);
        return encodedOperands;
    }

    public void decodeResult(String str){
        String string = str;
        String[] stringArray = string.split("\\|");
        int operator = Integer.parseInt(stringArray[0]);
        String[] operands = stringArray[1].split(",");
        
        if(operator == 5){
            double operand3 = Double.parseDouble(operands[0]);
            result.setText(Double.toString(operand3));
        }
        else{
            result.setText("error 404");
        }
    
    }

}