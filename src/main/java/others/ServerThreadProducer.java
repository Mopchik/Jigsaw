package others;

import java.util.ArrayList;

/**
 * This object calls in JigsawServerThread endEvent when client
 * ends the game. And exitEvent when client exits. JigsawServer
 * listens events and reacts to them.
 */
public class ServerThreadProducer
{
    private ArrayList<ServerThreadListener> listeners = new ArrayList<ServerThreadListener>();

    public void addListener(ServerThreadListener listener)
    {
        listeners.add(listener);
    }
    public ServerThreadListener[] getListeners()
    {
        return listeners.toArray(new ServerThreadListener[listeners.size()]);
    }

    public void removeListener(ServerThreadListener listener)
    {
        listeners.remove(listener);
    }

    public void endHappend(int steps, int time, int ind)
    {
        EndEvent ev = new EndEvent(this, steps, time, ind);
        for(ServerThreadListener listener : listeners)
            listener.endHappend(ev);
    }

    public void exitHappened(int ind){
        ExitEvent ev = new ExitEvent(this, ind);
        for(ServerThreadListener listener : listeners)
            listener.exitHappend(ev);
    }
}