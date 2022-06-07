package others;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameResultsTest {

    @Test
    void compare() {
        GameResults gr = new GameResults();
        gr.steps = 10;
        gr.time = 20;

        GameResults gr2 = new GameResults();
        gr2.steps = 8;
        gr2.time = 22;

        GameResults gr3 = new GameResults();
        gr3.steps = 10;
        gr3.time = 25;

        assert GameResults.compare(gr, gr2);
        assert GameResults.compare(gr3, gr2);
        assert GameResults.compare(gr, gr3);
    }
}