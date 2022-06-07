package others;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * These tests also check ServerThreadListener, EndEvent, ExitEvent.
 */
class ServerThreadProducerTest {

    int[] endExitIndexes;
    GameResults gameResults = new GameResults();

    class TestListener implements ServerThreadListener{
        @Override
        public void endHappend(EndEvent myEvent) {
            endExitIndexes[0] = myEvent.getInd();
            gameResults.time = myEvent.getTime();
            gameResults.b = true;
            gameResults.steps = myEvent.getSteps();
        }
        @Override
        public void exitHappend(ExitEvent myEvent) {
            endExitIndexes[1] = myEvent.getInd();
        }
    }


    @Test
    void endHappend() {
        ServerThreadProducer stp = new ServerThreadProducer();
        endExitIndexes = new int[]{-1, -1};
        stp.addListener(new TestListener());
        stp.endHappend(5, 7, 11);

        assert gameResults.b;
        assertEquals(11, endExitIndexes[0]);
        assertEquals(5, gameResults.steps);
        assertEquals(7, gameResults.time);
    }

    @Test
    void exitHappened() {
        ServerThreadProducer stp = new ServerThreadProducer();
        endExitIndexes = new int[]{-1, -1};
        stp.addListener(new TestListener());
        stp.exitHappened(8);
        assertEquals(8, endExitIndexes[1]);
    }
}