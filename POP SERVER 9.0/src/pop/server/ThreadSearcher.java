package pop.server;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.*;

/**
 *
 * @author Anmar Hindi
 */
public class ThreadSearcher {

    private Lock lock = new ReentrantLock();
    static volatile String lookUpThread = "";
    JFrame frame = new JFrame("Search for active Threads");
    JPanel panel = new JPanel();
    JTextArea textAr = new JTextArea();
    static volatile boolean newForum = false;
    ResultLogger resultLogger = new ResultLogger();
    ServerSocket server;
    ResultCommunicator rCommunicator;
    String name = "notSet";
    int port;

    public ThreadSearcher(int num, String name) {
        this.name = name;
        port = num;
        SeBeThread sb = new SeBeThread();
        sb.start();
    }

    public synchronized void setForum(boolean bool) {
        newForum = bool;
    }

    public boolean getForum() {
        return newForum;
    }

    class SeBeThread extends Thread {

        @Override
        public void run() {
            //drawWindow();
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            textAr.setText("PORT_OPEN");
            panel.setBackground(Color.white);
            panel.add(textAr);
            panel.setPreferredSize(new Dimension(200, 200));
            frame.getContentPane().add(panel);
            frame.pack();
            frame.setVisible(true);
            try {
                server = new ServerSocket(port);
                System.out.println("This is the" + name + " server, listening on port: " + port);
            } catch (IOException e) {
                //nothing done
            }

            resultLogger.start();

            Thread thread = new Thread(new ThreadText());

            thread.start();

            while (true) {
                try {
                    Socket socket = server.accept();

                    rCommunicator = new ResultCommunicator(socket, resultLogger);

                    resultLogger.addResultCommunicator(socket, rCommunicator);

                    rCommunicator.start();
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
                    String listOfK = "";
                    for (KeywordSearchServer k : MainDomain.serverCollector) {
                        listOfK += k.name + "\n";
                    }
                    //textAr.setText("" + MainDomain.serverCollector.size());
                    textAr.setText(listOfK);

                    Collection c = resultLogger.deviceIP.values();
                    Iterator it = c.iterator();
                    while (it.hasNext()) {
                        ResultCommunicator rcc = (ResultCommunicator) it.next();
                        if (rcc.receivedM.contains("create")) {
                            KeywordSearchServer.newForum = true;
                            KeywordSearchServer.temp = rcc.receivedM;
                            rcc.receivedM = "";
                        }
                    }

                    //if (rCommunicator.receivedM.contains("create")) {
                        
                        //String temm = rCommunicator.receivedM.replace("create", "");
                        //for (int i = 0; i < MainDomain.serverCollector.size(); i++) {
                        //KeywordSearchServer ktemp = MainDomain.serverCollector.get(i);
                        //if (!ktemp.name.equals(temm)) {
                        
                        //KeywordSearchServer.newForum = true;
                        //KeywordSearchServer.temp = rCommunicator.receivedM;
                        //rCommunicator.receivedM = "";
                        
                        //}
                        //}

                    //}
                    
                } catch (Exception e) {
                    // nothing done
                } finally {
                    lock.unlock();
                }

            }
        }
    }

}
