import connection.JigsawClientRun;
import connection.JigsawServerRun;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * IMPORTANT!! Run "runServerStart.bat" first.
 * This class allow you to run the server and two clients automatically.
 */
public class Main{
    public static void main(String[] args) {
        /*
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                JigsawServerRun.main(new String[]{"2", "5000", "120"});
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                JigsawClientRun.main(new String[]{"localhost", "5000", "Kostya"});
            }
        });
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                JigsawClientRun.main(new String[]{"localhost", "5000", "Vanya"});
            }
        });
        thread1.start();
        thread2.start();
        thread3.start();

         */
        Enumeration e = null;
        try {
            e = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        while(e.hasMoreElements()) {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {
                InetAddress i = (InetAddress) ee.nextElement();
                System.out.println(i.getHostAddress());
            }
        }
    }
}
