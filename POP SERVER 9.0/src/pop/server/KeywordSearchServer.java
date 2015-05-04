package pop.server;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.*;

/**
 * 
 * @author Anmar Hindi
 */
public class KeywordSearchServer {

    private Lock lock = new ReentrantLock();
    static volatile String temp = "";
    JFrame frame = new JFrame("Server");
    JPanel panel = new JPanel();
    JTextArea textAr = new JTextArea();
    static volatile boolean newForum = false;
    MessageLogger messageLogger = new MessageLogger();
    ServerSocket server;
    Communicator communicator;
    String name = "notSet";
    int port;
    
    public KeywordSearchServer(int num, String name) {
        this.name = name;
        port = num;
        MainDomain.serverCollector.add(this);
        ConnectorThread ct = new ConnectorThread();
        ct.start();
        newForum = false;
    }

    public synchronized void setForum(boolean bool) {
        newForum = bool;
    }

    public boolean getForum() {
        return newForum;
    }

    class ConnectorThread extends Thread {

        @Override
        public void run() {
            //drawWindow();
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            textAr.setText("PORT_OPEN");
            panel.setBackground(Color.white);
            panel.add(textAr);
            panel.setPreferredSize(new Dimension(100, 150));
            frame.getContentPane().add(panel);
            frame.pack();
            frame.setVisible(true);
            try {
                server = new ServerSocket(port);
                System.out.println("This is the " + name + " server, listening on port: " + port);
            } catch (IOException e) {
                //
            }

            messageLogger.start();

            Thread thread = new Thread(new ThreadText());

            thread.start();

            while (true) {
                try {
                    Socket socket = server.accept();
                    //multiple communicators assigned to a single messagelogger
                    communicator = new Communicator(socket, messageLogger);

                    messageLogger.addCommunicator(socket, communicator);

                    communicator.start();
                } catch (IOException e) {
                    //
                }

            }
        }
    }

    class ThreadText implements Runnable {

        
        @Override
        public void run() {

            while (true) {
                lock.lock();
                try {
                    String listTemp = "";
                    listTemp += communicator.nextMessageTemp + "\n";
                    
                    textAr.setText(listTemp);
                    //textAr.setText(communicator.nextMessageTemp);
                } catch (Exception e) {
                    
                } finally {
                    lock.unlock();
                }

            }
        }
    }

}
