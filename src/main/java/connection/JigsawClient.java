package connection;

import base.ClientBaseManager;
import others.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLException;

/**
 * This class is representing the client of the Jigsaw game.
 * It connects to the server and communicates with it by
 * requests: command + string.
 */
public class JigsawClient {

    private Socket clientSocket;
    private BufferedReader br;
    private PrintStream ps;
    private ClientBaseManager cbm;
    private final String name;

    public JigsawClient(String serverHostStr, int serverPort, String name) throws IOException, SQLException {
        this.name = name;
        InetAddress serverHost = InetAddress.getByName(serverHostStr);

        cbm = new ClientBaseManager(serverHostStr, 1527);
        clientSocket = new Socket(serverHost, serverPort);

        br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ps = new PrintStream(clientSocket.getOutputStream());
    }

    public String getRequest() throws IOException {
        String s = br.readLine();
        if(s == null){
            throw new IOException();
        }
        return s;
    }

    public void sendRequest(Command c, String params){
        StringBuilder strb = new StringBuilder();
        strb.append(c.ordinal());
        strb.append(" " + params);
        ps.println(strb);
        ps.flush();
    }

    public void sendRequest(Command c){
        StringBuilder strb = new StringBuilder();
        strb.append(c.ordinal());
        ps.println(strb);
        ps.flush();
    }

    public void close() {
        try {
            clientSocket.close();
            cbm.close();
        } catch (IOException e){

        } catch (SQLException e) {

        }
    }

    /**
     * Writes result of the game to the base.
     * @param login
     * @param steps
     * @param time
     * @throws SQLException
     */
    public void writeToTable(String login, int steps, String time) throws SQLException {
        cbm.writeToTable(login, steps, time);
    }

    /**
     * Gets 10 best sorted results from the table.
     * @return
     */
    public String getTableResults(){
        return cbm.getTableResults();
    }

    public String getName(){
        return name;
    }
}
