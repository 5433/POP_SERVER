package pop.server;

import java.io.*;

import java.net.*;

 

public class ClientListener extends Thread

{
	String messageM = "";

    private ServerDispatcher mServerDispatcher;

    private ClientInfo mClientInfo;

    private BufferedReader mIn;

 

    public ClientListener(ClientInfo aClientInfo, ServerDispatcher aServerDispatcher)

    throws IOException

    {

        mClientInfo = aClientInfo;

        mServerDispatcher = aServerDispatcher;

        Socket socket = aClientInfo.mSocket;

        mIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    }

	public synchronized void changeString(String a){
		messageM = a;
	}
 
 	public String getMessage(){
 		return messageM;
 	}

    /**

     * Until interrupted, reads messages from the client socket, forwards them

     * to the server dispatcher's queue and notifies the server dispatcher.

     */

    public void run()

    {

        try {

           while (!isInterrupted()) {

               String message = mIn.readLine();
			   //messageM += message + "\r\n";
			   messageM = message;
			   
               if (message == null)

                   break;

               mServerDispatcher.dispatchMessage(mClientInfo, message);

           }

        } catch (IOException ioex) {

           // Problem reading from socket (communication is broken)

        }

 

        // Communication is broken. Interrupt both listener and sender threads

        mClientInfo.mClientSender.interrupt();

        mServerDispatcher.deleteClient(mClientInfo);

    }

 

}