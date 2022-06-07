package others;

import java.util.EventObject;

/**
 * This class has information which server must know when
 * the client ends each game.
 */
public class EndEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    private int time;
    public int getTime(){return time;}
    private int steps;
    public int getSteps(){return steps;}
    private int ind;
    public int getInd(){return ind;}
    public EndEvent(Object source, int steps, int time, int ind){
        super(source);
        this.time = time;
        this.steps = steps;
        this.ind = ind;
    }
}

