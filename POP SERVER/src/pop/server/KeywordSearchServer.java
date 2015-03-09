package pop.server;

import java.io.*;
import java.net.*;
import java.awt.*;

import javax.swing.*;

public class KeywordSearchServer{

	String temp = "";
	JFrame frame = new JFrame("Server");
	JPanel panel = new JPanel();
	JTextArea textAr = new JTextArea();
	static boolean newForum = false;
	ServerDispatcher serverDispatcher = new ServerDispatcher();
	ServerSocket server;
	ClientListener clientListener;
	
	public KeywordSearchServer(int num, String name){
		connector(num,name);
		
	}
 
 	public String getMessage(){		
 		return clientListener.messageM;
 	}
	
 	public void setForum(boolean bool){
 		
		newForum = bool;
 			
 	}
	
	public boolean getForum(){
		return newForum;
	}
	
 	public void connector(int i, String name){
		drawWindow();	
        try{
			server = new ServerSocket(i);
			System.out.println("This is the " + name + " server, listening on port: " + i);
        }catch(IOException e){
        	e.printStackTrace();
        }
        // Start ServerDispatcher thread             

        

        serverDispatcher.start();

	    Thread thread = new Thread(new ThreadText());
		
 	    thread.start();

        // Accept and handle client connections

        while (true) {
		
           try {			   
			   
               Socket socket = server.accept();

               ClientInfo clientInfo = new ClientInfo();

               clientInfo.mSocket = socket;

               ClientListener clientListener =

                   new ClientListener(clientInfo, serverDispatcher);

               ClientSender clientSender =

                   new ClientSender(clientInfo, serverDispatcher);			   
				
               clientInfo.mClientListener = clientListener;

               clientInfo.mClientSender = clientSender;
			   
			   this.clientListener = clientListener;

               clientListener.start();

               clientSender.start();		   
			   
               serverDispatcher.addClient(clientInfo);
			   			   
			   		   		  
			   
           } catch (IOException ioe) {

               ioe.printStackTrace();

           }

        }
 	}
	
	
	
	public void drawWindow(){
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		textAr.setText("PORT_OPEN");
		panel.add(textAr);
		panel.setPreferredSize(new Dimension(400,600));
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
		
	}
	
	
	class ThreadText implements Runnable{
		public void run(){
			while(true){								
				try{
					textAr.setText(clientListener.messageM);
					if(clientListener.messageM.contains("create")){	
						setForum(true);							
					}
				}catch(Exception e){
					//e.printStackTrace();
				}
				
			}
		}
	}
	
	
}