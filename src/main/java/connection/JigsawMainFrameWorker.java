package connection;

import graphics.MainFrame;
import logics.Figure;
import others.Command;
import others.RequestObjectConverter;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class do some work between client and frame. It manages
 * request from client and do some actions with the frame
 * according to them. Also it listens buttons on the frame and
 * sends from client to server needed requests.
 */
public class JigsawMainFrameWorker {

    private JigsawClient jc;
    private MainFrame mf;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<Figure.TypeOfFigure> figures= new ArrayList<>();
    private int duration = 0;
    private int stepper = -1;
    private int maxDuration = -1;
    private Timer timer = new Timer(100, event -> doTimer());

    /**
     * This method runs every 100ms and changes seconds on timer on
     * the frame every 10 steps which are 1 second.
     */
    private void doTimer(){
        mf.buttons.remove(mf.steps);
        mf.steps = new JTextArea();
        mf.steps.append("Steps:   " + mf.bigPanel.steps());
        mf.steps.append("\nTimer:   " + getTimeDuration(duration));
        if (maxDuration != -1) {
            mf.steps.append("\nMax duration:   " + getTimeDuration(maxDuration));
        }
        mf.buttons.add(mf.steps);
        if(stepper++ % 10 == 0) {
            stepper = 1;
            duration++;
            if (duration > maxDuration && maxDuration != -1) {
                onEnd();
            }
        }
    }

    public JigsawMainFrameWorker(JigsawClient jc){
        this.jc = jc;
    }

    /**
     * Opens frame, starts getting requests from server and
     * sending requests to it by actionListeners on buttons.
     */
    public void start(){
        mf = new MainFrame();
        mf.smallPanel.clear();

        mf.end.addActionListener(event -> onEnd());
        mf.newGame.addActionListener(event -> onNewGame());
        mf.top10Button.addActionListener(event -> onTop10());
        mf.hideTop10.addActionListener(event -> onHideTop10());
        mf.exit.addActionListener(event -> onExit());
        mf.setOnDispose(() -> {
            jc.sendRequest(Command.EXIT);
        });

        mf.setWaitingMessage();

        doTimer();

        mf.setVisible(true);

        boolean shouldExit;
        do{
            try{
                shouldExit = !manageRequest(jc.getRequest());
            } catch (IOException e){
                JOptionPane.showMessageDialog(null, "Problems with connection, probably\n" +
                        "server shutdown. Jigsaw closed.");
                mf.dispose();
                shouldExit = true;
            }
        } while(!shouldExit);

        jc.close();
        System.out.println("Connection closed");
    }

    /**
     * Looks at the command from the request and does the action according to it.
     * @param request
     * @return False if user pressed "Exit" or there are some problems with connection.
     * @throws IOException
     */
    public boolean manageRequest(String request) throws IOException {
        String[] strs = request.split(" ");
        switch (Command.values()[Integer.parseInt(strs[0])]) {
            case CAN_START -> {
                int winner = Integer.parseInt(strs[1]);
                doWhenCanStart(winner);
            }
            case EXIT -> {
                return false;
            }
            case FIGURES -> {
                figures = RequestObjectConverter.readFigures(strs);
                mf.smallPanel.updateFigures(figures);
            }
            case NAMES -> {
                names = new ArrayList<String>();
                for (int i = 1; i < strs.length; i++) {
                    names.add(strs[i]);
                }
            }
            case YOUR_NAME -> {
                jc.sendRequest(Command.YOUR_NAME, jc.getName());
            }
            case OTHER_EXIT -> {
                doWhenOtherExit(strs[1]);
            }
            case MAX_DURATION -> {
                maxDuration = Integer.parseInt(strs[1]);
            }
        }
        return true;
    }

    /**
     * Stops the current game and send to the server a message about that.
     * Is called when button "End" is pressed and when the timer is out.
     */
    public void onEnd(){
        mf.smallPanel.clear();
        mf.buttons.remove(mf.end);
        mf.buttons.add(mf.top10Button);
        mf.setWaitingMessage();
        jc.sendRequest(Command.END, getSteps() + " " + duration);
        timer.stop();
        try {
            jc.writeToTable(jc.getName(), getSteps(), getTimeDuration(duration));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Unable to enter the game result into base.");
        }
    }

    /**
     * Shows top 10 best results.
     */
    public void onTop10(){
        mf.top10 = new JTextArea();
        mf.top10.append("TOP 10:\n");
        mf.top10.append(jc.getTableResults());
        mf.top10Panel.add(mf.top10);
        mf.smallPanel.setVisible(false);
        mf.buttons.remove(mf.top10Button);
        mf.buttons.add(mf.hideTop10);
    }

    /**
     * Hides top 10 results. Can be used only when
     * results are visible.
     */
    public void onHideTop10(){
        mf.top10Panel.removeAll();
        mf.smallPanel.setVisible(true);
        mf.buttons.remove(mf.hideTop10);
        mf.buttons.add(mf.top10Button);
    }

    /**
     * Starting new game.
     */
    public void onNewGame(){
        mf.bigPanel.clear();
        mf.smallPanel.refresh();
        duration = 0;
        stepper = 0;
        timer.start();
        mf.buttons.remove(mf.top10Button);
        mf.buttons.remove(mf.hideTop10);
        mf.buttons.remove(mf.newGame);
        mf.top10Panel.removeAll();
        mf.smallPanel.setVisible(true);
        mf.buttons.add(mf.end);
        //mf.buttons.add(mf.exit);
        mf.setNames(-1, names);
    }

    public void onExit(){
        mf.dispose();
    }

    public void doWhenCanStart(int winner) throws IOException {
        jc.sendRequest(Command.FIGURES);
        manageRequest(jc.getRequest());
        jc.sendRequest(Command.NAMES);
        manageRequest(jc.getRequest());
        mf.setNames(winner, names);
        mf.buttons.add(mf.newGame);
    }

    /**
     * Stops the game when other exited the game.
     * @param nameOfExit Name of player that exited the game.
     */
    public void doWhenOtherExit(String nameOfExit){
        mf.buttons.removeAll();
        mf.buttons.add(mf.exit);
        mf.buttons.add(mf.top10Button);
        mf.smallPanel.clear();
        mf.top10Panel.removeAll();
        mf.smallPanel.setVisible(true);
        mf.playersPanel.removeAll();
        mf.names = new JTextArea();
        mf.names.append(nameOfExit + " left the server.\n You are a WINNER!\n");
        mf.setWaitingMessage();
        mf.playersPanel.add(mf.names);
    }

    public static String getTimeDuration(long duration){
        long s = duration % 60;
        long m = (duration / 60) % 60;
        long h = (duration / 3600) % 60;
        return "" + h + "h:" +  m + "m:"  + s + "s";
    }

    public int getSteps(){
        return mf.bigPanel.steps();
    }

}
