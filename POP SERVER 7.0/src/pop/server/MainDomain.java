package pop.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Anmar Hindi
 */
public final class MainDomain {

    int i = 8888;
    String mes = "";
    KeywordSearchServer kss;
    ThreadSearcher threadServer;
    //static HashMap<Integer,KeywordSearchServer> serverCollector = new HashMap<>();
    static ArrayList<KeywordSearchServer> serverCollector = new ArrayList<>();
    static HashMap<Integer, ThreadSearcher> searchServerCollector = new HashMap<>();
    private final Lock lock = new ReentrantLock();

    public MainDomain() {
        executeService();
    }

    public void executeService() {

        try {
            lock.lock();
            Thread sThread = new Thread(new NewSearchThread());
            sThread.start();
        } finally {
            lock.unlock();
        }

        Thread thread = new Thread(new NewRunThread());
        Thread testThread = new Thread(new TestRunThread());
        Thread testThreadTwo = new Thread(new TestRunThreadTwo());

        thread.start();
        testThread.start();
        testThreadTwo.start();

        while (true) {
            try {
                if (KeywordSearchServer.newForum == true) {
                    i++;
                    mes = KeywordSearchServer.temp;
                    String tm = mes.replace("create", "");
                    System.out.println(mes);
                    KeywordSearchServer kes = new KeywordSearchServer(i, tm.replace(" ", ""));
                    System.out.println("SUCCESS");

                }
            } catch (NullPointerException e) {
                // do nothing
            }

        }
    }

    class TestRunThread implements Runnable {

        @Override
        public void run() {
            KeywordSearchServer kos = new KeywordSearchServer(8900, "Apples");
        }
    }

    class TestRunThreadTwo implements Runnable {

        @Override
        public void run() {
            KeywordSearchServer koos = new KeywordSearchServer(8901, "UK-news");
        }
    }

    class NewRunThread implements Runnable {

        @Override
        public void run() {
            //adds itself to the ArrayList within constructor.
            kss = new KeywordSearchServer(i, "Main");
        }
    }

    class NewSearchThread implements Runnable {

        @Override
        public void run() {
            searchServerCollector.put(7777, threadServer);
            threadServer = new ThreadSearcher(7777, " Search");
        }
    }

}
