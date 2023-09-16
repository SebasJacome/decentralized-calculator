# Calculator ADSOA 
The purpose of this program is to make a calculator that is divided in three sections: 
1. Interface: Works as a client, it was build with JavaFX
2. Middleware: Which function is to deliver the messages to every single client/server connected to it, 
3. Server: Makes the operation and returns the answer.

At the moment, the calculator just accepts operations like this 2 + 2. 

This is a project that is just starting, and it is not finished but, every single version works correctly. 

The requirements for the first version of the project are:

* Implementation of a socket-server that allows multiple clients to connect.

* The server must keep track of incoming connections in a data array (run by the students).

* The server shall manage the removal of connections from the data array in case of connection loss.

* The server shall redirect to all connected clients any message received, except to the originating client.

* Implementation of a socket-client that establishes a connection to the server.

* The client must send messages with the following structure:
    * Message type (2 bytes): a service number representing an identifier of the operation. Content code
    * Operation information (variable array): this is the information of the message, which can be structured as 2 bytes representing the operation identifier. 
      structured as 2 bytes defining the size of the information, followed by the operation information.

* The client must receive the messages from the server and print them on the standard output.

* Design of a graphical interface using SceneBuilder with the skeleton of a calculator.

