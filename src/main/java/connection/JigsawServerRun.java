package connection;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;

/**
 * IMPORTANT!! To start new game you also need to run "runServerStart.bat"
 * on your server.
 * Enter "exit" in console to stop the server.
 * Arguments: {Number of players} {Server Port} {Max duration of each game}
 *             2 5000 300
 *             java -jar Jigsaw.jar 2 5000 300
 */
public class JigsawServerRun {
    public static void main(String[] args) {
        ServerSocket jigsawServerSocket = null;
        JigsawServer js = null;
        try {
            int numOfPlayers = Integer.parseInt(args[0]);
            int serverPort = Integer.parseInt(args[1]);
            int maxDuration = Integer.parseInt(args[2]);
            jigsawServerSocket = new ServerSocket(serverPort);

            js = new JigsawServer(jigsawServerSocket, numOfPlayers, maxDuration);
            JigsawServer finalJs = js;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    finalJs.start();
                }
            });
            thread.start();
            System.out.println("Server started. To stop it enter \"exit\"");

            /**
             * Needs this JFrame because user can accidentally close the command line (cmd or terminal).
             * It helps to not make server be not closable.
             */
            JFrame jFrame = new JFrame();
            jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            jFrame.add(new JTextArea("Close window to stop the server."));
            jFrame.setBounds(400, 300, 400, 200);
            jFrame.setVisible(true);

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while(!in.readLine().equals("exit"));
            jFrame.dispose();
            thread.interrupt();
            js.close();
        } catch(IOException e){
            if(js != null){
                js.close();
            }
            try {
                if(jigsawServerSocket != null){
                    jigsawServerSocket.close();
                }
            } catch (IOException ex) {
            }
        }
    }
}
