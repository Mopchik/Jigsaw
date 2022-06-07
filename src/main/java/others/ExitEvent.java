package others;

import java.util.EventObject;

/**
 * This class has information which server must know when
 * the client exits each game.
 */
public class ExitEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    private int ind;
    public int getInd(){return ind;}
    public ExitEvent(Object source, int ind) {
        super(source);
        this.ind = ind;
    }
}
