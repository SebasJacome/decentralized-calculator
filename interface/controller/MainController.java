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
import mensaje.DecoderEncoder;
import mensaje.Mensaje;

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

    private Mensaje m;

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

    public void processOperator(ActionEvent event){
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
            m = new Mensaje(true);
            this.sentMessages.add(m.getHashIdentifier());
            System.out.println("Escribi un msj con este hash: " + m.getHashIdentifier());
            isOperator = false;
            start = true;
            m.setTipoOperacion(encodeOperator(operator));
            double[] encodedOperands = encodeOperands(result.getText());
            String encodedOperation = encodedOperands[0] + "," + encodedOperands[1];
            m.setDatos(encodedOperation.getBytes());
            sendOperation(m);
        }
    }

    public void sendOperation(Mensaje m){
        try{
            DecoderEncoder.escribir(out, m);
        }
        catch(IOException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void receiveResponses() {
        try {
            while (true) {
                Mensaje respuesta = DecoderEncoder.leer(in);
                String response = respuesta.getTipoOperacion() + "|" + respuesta.getDatosString();
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


    public double[] encodeOperands(String operands){
        double[] encodedOperands = new double[2];
        String[] operandsArray = operands.split("\\+|\\-|\\*|\\/");
        encodedOperands[0] = Double.parseDouble(operandsArray[0]);
        encodedOperands[1] = Double.parseDouble(operandsArray[1]);
        return encodedOperands;
    }

    public void decodeResult(String str){
        String string = str;
        String[] stringArray = string.split("\\|");
        int operator = Integer.parseInt(stringArray[0]);
        String[] operands = stringArray[1].split(",");
        
        if(operator == 5){
            double operand3 = Double.parseDouble(operands[2]);
            result.setText(Double.toString(operand3));
        }
        else{
            result.setText("error 404");
        }
    
    }

}