package connection;

import logics.Figure;
import others.Command;
import others.RequestObjectConverter;
import others.ServerThreadProducer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This class runs on the server and communicates with the client.
 * It gets requests from client, manages them and send answers.
 */
public class JigsawServerThread extends Thread {

    final Socket clientSocket;
    public ServerThreadProducer onEndOrExit = new ServerThreadProducer();
    private PrintStream ps;
    private BufferedReader br;
    private String clientsName;
    ArrayList<Figure.TypeOfFigure> figs = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    public int ind;

    /**
     *
     * @param s
     * @param ind It is the index of this thread in JigsawServer threads collection.
     *            It is needed for actions when the client ends game or exits.
     */
    JigsawServerThread(Socket s, int ind) {
        clientSocket = s;
        this.ind = ind;
        try {
            ps = new PrintStream(clientSocket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets figures from the server and remembers them.
     * @param newFigs
     */
    public void updateFigs(ArrayList<Figure.TypeOfFigure> newFigs){
        this.figs.clear();
        for(var f: newFigs){
            this.figs.add(f);
        }
    }

    /**
     * Gets names of players from the server and remembers them.
     * @param names Names of all players in the present Jigsaw game.
     */
    public void updateNames(ArrayList<String> names){
        this.names.clear();
        for(int i = 0; i < names.size(); i++){
            if(i!=ind)
                this.names.add(names.get(i));
            else
                this.names.add(names.get(i) + "(YOU)");
        }
    }

    /**
     * Runs this thread. It gets requests from the client and manages them.
     */
    @Override
    public void run () {
        boolean shouldExit;
        do {
            try{
                shouldExit = !manageRequest(getRequest());
            }
            catch (IOException e){
                shouldExit = true;
            }
        } while(!shouldExit);

        try {
            clientSocket.close();
        } catch(IOException e){

        }
    }

    public String getRequest() throws IOException {
        String s = "";
        s = br.readLine();
        return s;
    }

    /**
     * Looks at the command from the request and does the action according to it.
     * @param request
     * @return False if user pressed "Exit".
     */
    public boolean manageRequest(String request){
        String[] strs = request.split(" ");
        switch(Command.values()[Integer.parseInt(strs[0])]){
            case END -> {
                onEndOrExit.endHappend(Integer.parseInt(strs[1]), Integer.parseInt(strs[2]), ind);
                break;
            }
            case FIGURES -> sendFiguresCommand();
            case NAMES -> sendNamesCommand();
            case YOUR_NAME -> clientsName = strs[1];
            case EXIT -> {
                sendAnswer(Command.EXIT);
                onEndOrExit.exitHappened(ind);
                return false;
            }
        }
        return true;
    }

    public void sendAnswer(Command c){
        ps.println(c.ordinal());
        ps.flush();
    }

    public void sendAnswer(Command c, String params){
        StringBuilder strb = new StringBuilder();
        strb.append(c.ordinal());
        strb.append(" " + params);
        ps.println(strb);
        ps.flush();
    }

    /**
     * manageRequest will change the clientsName variable to the needed.
     * @return The name of the client this thread works to.
     * @throws IOException
     */
    public String getClientsName() throws IOException {
        sendAnswer(Command.YOUR_NAME);
        manageRequest(getRequest());
        return clientsName;
    }

    public void sendStartCommand(int winner){
        sendAnswer(Command.CAN_START, String.valueOf(winner));
    }

    public void sendMaxDuration(int maxDuration){
        sendAnswer(Command.MAX_DURATION, String.valueOf(maxDuration));
    }

    public void sendOtherExitCommand(String nameOfExit){
        sendAnswer(Command.OTHER_EXIT, nameOfExit);
    }

    public void sendFiguresCommand(){
        sendAnswer(Command.FIGURES, RequestObjectConverter.convertFiguresToRequest(figs));
    }

    public void sendNamesCommand(){
        sendAnswer(Command.NAMES, RequestObjectConverter.convertNamesToRequest(names));
    }

}