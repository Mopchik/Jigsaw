package connection;


import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * IMPORTANT!! Run this class after you ran JigsawServer and
 * "runServerStart.bat". This class runs JigsawClient and starts
 * the game if everything is OK.
 * java -jar Jigsaw.jar localhost 5000 Kostichka
 */
public class JigsawClientRun {

    public static void main(String[] args) {
        String serverHost = args[0];
        int serverPort = Integer.parseInt(args[1]);
        String name = args[2];
        JigsawClient jc = null;
        try {
            jc = new JigsawClient(serverHost, serverPort, name);
            JigsawMainFrameWorker jmfw = new JigsawMainFrameWorker(jc);
            jmfw.start();
        } catch (IOException e){
            JOptionPane.showMessageDialog(null, "Unable to connect to the server.");
            if(jc != null){
                jc.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Problems with base, unable to start.");
            if(jc != null){
                jc.close();
            }
            e.printStackTrace();
        }
    }
}