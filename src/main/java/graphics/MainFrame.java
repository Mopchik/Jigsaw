package graphics;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * This class is the window which opens on the client's computer.
 * It contains big field, small field, buttons and textAreas.
 */
public class MainFrame extends JFrame {

    public GeneratedSmallField smallPanel;
    public JigsawBigField bigPanel;

    public static int SMP_WIDTH = 240;
    public static int SMP_HEIGHT = 240;
    public static int BP_WIDTH = 450;
    public static int BP_HEIGHT = 450;
    public static int FR_WIDTH = 1200;
    public static int FR_HEIGHT = 630;
    public static int x;
    public static int y;

    public JPanel buttons = new JPanel();
    public JPanel playersPanel = new JPanel();
    public JPanel top10Panel = new JPanel();
    public JButton exit = new JButton("Exit");
    public JButton end = new JButton("End game");
    public JButton newGame = new JButton("New game");
    public JButton top10Button = new JButton("TOP 10");
    public JButton hideTop10 = new JButton("Hide TOP 10");
    public JTextArea names = new JTextArea();
    public JTextArea top10 = new JTextArea();
    public JTextArea steps = new JTextArea();

    public MainFrame() {
        initUI();
    }

    /**
     * Setting names and adding the "IS WINNER" to the winner.
     * @param winner Index of winner. If there is not winner set it "-1".
     * @param namesStr
     */
    public void setNames(int winner, ArrayList<String> namesStr){
        playersPanel.removeAll();
        this.names = new JTextArea();
        for(int i = 0; i < namesStr.size(); i++){
            if(i != winner)
                names.append(namesStr.get(i) + "\n");
            else {
                names.append(namesStr.get(i) + " is WINNER!\n");
            }
        }
        playersPanel.add(names);
    }

    /**
     * Dispose method is needed as a listener of the window
     * disposing. When we set the disposeMethod from other class
     * it will be called when the window closes.
     */
    private Runnable disposeMethod;
    public void setOnDispose(Runnable exit){
        this.disposeMethod = exit;
    }
    @Override
    public void dispose(){
        disposeMethod.run();
        super.dispose();
    }

    public void setWaitingMessage(){
        names.append("Waiting others\n");
    }


    private void initUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        x = dimension.width / 2 - FR_WIDTH / 2;
        y = dimension.height / 2 - FR_HEIGHT / 2;
        setBounds(x, y, FR_WIDTH, FR_HEIGHT);
        setTitle("Jigsaw");

        smallPanel = new GeneratedSmallField(FR_WIDTH - SMP_WIDTH - 30, 20, SMP_WIDTH, SMP_HEIGHT);
        smallPanel.setVisible(true);
        add(smallPanel);

        bigPanel = new JigsawBigField(0, 0, BP_WIDTH, BP_HEIGHT, smallPanel);
        bigPanel.setVisible(true);
        add(bigPanel);

        buttons.add(steps);
        buttons.add(exit);
        buttons.add(top10Button);
        playersPanel.add(names);
        top10Panel.add(top10);

        add(playersPanel, BorderLayout.NORTH);
        add(top10Panel, BorderLayout.LINE_END);
        add(buttons, BorderLayout.PAGE_END);
    }
}
