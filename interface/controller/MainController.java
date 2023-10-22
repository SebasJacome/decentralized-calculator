package controller;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.Button;

import java.io.*;
import java.net.*;

import mensaje.DecoderEncoder;

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

    public MainController() {
        // Initialize the socket connection in the constructor
        final String HOST = "localhost"; // Host del middleware
        final int PORT = 12345; // Puerto del middleware

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
            isOperator = false;
            start = true;
            int encodedOperator = encodeOperator(operator);
            double[] encodedOperands = encodeOperands(result.getText());
            String encodedOperation = encodedOperator + "|" + encodedOperands[0] + "," + encodedOperands[1];
            
            sendOperation(encodedOperation);
            
        }
        
        
    }

    public void sendOperation(String operation){
        byte[] buffer = operation.getBytes();      
        try{
            out.write(buffer);
            out.flush();
        }
        catch(IOException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void receiveResponses() {
        try {
            while (true) {
                byte[] buffer = new byte[1024];
                int bytesRead = in.read(buffer);
                String response = new String(buffer, 0, bytesRead);
               
                if (response != "") {
                    // Update the UI with the received response
                    updateUIWithResponse(response);
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
                decodeResult(response);
            }
        });
        
    }
    
    public int encodeOperator(String operator){
        

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
        
        if(operator == 100){
            double operand3 = Double.parseDouble(operands[2]);
            result.setText(Double.toString(operand3));
        }
        else{
            result.setText("error 404");
        }
    
    }

}