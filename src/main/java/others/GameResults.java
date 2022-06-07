package others;

/**
 * This class is used in JigsawServer as the results of the game
 * of each client.
 */
public class GameResults {
    public boolean b;
    public int steps;
    public int time;

    public GameResults(){b = false; steps = -1; time = -1;}

    /**
     * Checks that result of first client are better than the second one.
     * @param f
     * @param s
     * @return
     */
    public static boolean compare(GameResults f, GameResults s){
        if(f.steps > s.steps) return true;
        if(f.steps < s.steps) return false;
        return f.time < s.time;
    }
}