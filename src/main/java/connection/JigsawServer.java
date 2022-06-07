/*
program args: 2 5000
 */
package connection;

import logics.Figure;
import others.EndEvent;
import others.ExitEvent;
import others.GameResults;
import others.ServerThreadListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This class represents the Jigsaw server. It waits people to be
 * numOfPlayers, it generates new figures and starts the game. Also,
 * it finds a winner of the game and react when somebody exits.
 */
public class JigsawServer {

    ArrayList<Figure.TypeOfFigure> genFigs;
    ArrayList<Socket> sockets = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    ArrayList<GameResults> presentGames = new ArrayList<>();
    ArrayList<JigsawServerThread> threads = new ArrayList<>();
    ServerSocket jigsawServerSocket;
    int numOfPlayers, maxDuration;

    public JigsawServer(ServerSocket jigsawServerSocket, int numOfPlayers, int maxDuration) {
        this.jigsawServerSocket = jigsawServerSocket;
        this.numOfPlayers = numOfPlayers;
        this.maxDuration = maxDuration;
        for(int i =0;i < numOfPlayers;i++) {
            presentGames.add(new GameResults());
        }
    }

    public void start(){
        if(waitAllPeople()) {
            startNewGame(true);
        }
    }

    private void onEnd(int steps, int time, int ind){
        presentGames.get(ind).steps = steps;
        presentGames.get(ind).time = time;
        presentGames.get(ind).b = true;
        if(isAllTrue(presentGames)){
            startNewGame(false);
        }
    }

    private void onExit(int ind){
        threads.remove(ind);
        sockets.remove(ind);
        String name = names.get(ind);
        names.remove(ind);
        for(int i = ind; i < threads.size(); i++){
            threads.get(i).ind--;
        }
        for(var thread:threads){
            thread.sendOtherExitCommand(name);
        }
        if(threads.size() == numOfPlayers - 1) {
            start();
        }
    }

    /**
     * Waits while number of people is less than needed.
     * @return False if there are some problems with connection.
     */
    private boolean waitAllPeople(){
        int ind = threads.size();
        try {
            while(ind < numOfPlayers){
                sockets.add(jigsawServerSocket.accept());
                ind = threads.size();
                JigsawServerThread thread = new JigsawServerThread(sockets.get(ind), ind);
                thread.sendMaxDuration(maxDuration);
                thread.onEndOrExit.addListener(new ServerThreadListener() {
                    @Override
                    public void endHappend(EndEvent event) {
                        onEnd(event.getSteps(), event.getTime(), event.getInd());
                    }

                    @Override
                    public void exitHappend(ExitEvent myEvent) {
                        onExit(myEvent.getInd());
                    }
                });
                threads.add(thread);
                String name = thread.getClientsName();
                names.add(name);
                thread.start();
                ind = threads.size();
            }
            return true;
        }catch (IOException e){
            System.out.println("JigsawServerConnection closed.");
            return false;
        }
    }

    /**
     * Generate new figures. Find winner if the game is not first
     * and sending to the clients that they can start.
     * @param isFirstGame
     */
    private void startNewGame(boolean isFirstGame){
        genFigs = generateFigures();
        int indOfWinner = -1;
        if(!isFirstGame) {
            indOfWinner = 0;
            GameResults lower = presentGames.get(0);
            for (int i = 0; i < numOfPlayers; i++) {
                if (GameResults.compare(presentGames.get(i), lower)) {
                    lower = presentGames.get(i);
                    indOfWinner = i;
                }
            }
        }
        for (int i = 0; i < numOfPlayers; i++) {
            threads.get(i).updateFigs(genFigs);
            threads.get(i).updateNames(names);
            presentGames.get(i).b = false;
        }
        for(var thread:threads){
            thread.sendStartCommand(indOfWinner);
        }
    }

    /**
     * Generate 81 figures because it is the maximum of figures which can be
     * placed to the field.
     * @return
     */
    private ArrayList<Figure.TypeOfFigure> generateFigures(){
        ArrayList<Figure.TypeOfFigure> genFigs = new ArrayList<>();
        for(int i = 0; i < 81; i++){
            genFigs.add(Figure.createRandomType());
        }
        return genFigs;
    }

    /**
     *
     * @param b
     * @return True if all players ended their game.
     */
    private boolean isAllTrue(ArrayList<GameResults> b){
        for(int i = 0; i < b.size();i++){
            if(!b.get(i).b) return false;
        }
        return true;
    }

    /**
     * Close connections, stop threads, close serverSocket.
     * @throws IOException
     */
    public void close(){
        for (var s : sockets) {
            try {
                s.close();
            } catch(IOException e){}
        }
        for (var t : threads) {
            t.interrupt();
        }
        try {
            jigsawServerSocket.close();
        } catch (IOException e){}
    }

}

