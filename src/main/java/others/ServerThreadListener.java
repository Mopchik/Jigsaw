package others;

public interface ServerThreadListener
{
    void endHappend(EndEvent myEvent);
    void exitHappend(ExitEvent myEvent);
}
