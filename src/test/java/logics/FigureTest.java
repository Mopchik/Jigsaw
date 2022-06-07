package logics;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class FigureTest {

    @Test
    void moveFigure() {
        Figure figure = new Figure(Figure.TypeOfFigure.aa);
        figure.moveFigure(2,2);
        assertEquals(figure.points.length, 4);
        assert Arrays.stream(figure.points).anyMatch(it -> it.X == 2 && it.Y == 2);
        assert Arrays.stream(figure.points).anyMatch(it -> it.X == 3 && it.Y == 2);
        assert Arrays.stream(figure.points).anyMatch(it -> it.X == 2 && it.Y == 3);
        assert Arrays.stream(figure.points).anyMatch(it -> it.X == 4 && it.Y == 2);
    }

    @Test
    void tryMoveFigure() {
        Figure figure = new Figure(Figure.TypeOfFigure.aa);
        figure.tryMoveFigure(2,2, 0, 5, 0, 4);
        assertEquals(figure.points.length, 4);
        assert Arrays.stream(figure.points).anyMatch(it -> it.X == 2 && it.Y == 2);
        assert Arrays.stream(figure.points).anyMatch(it -> it.X == 3 && it.Y == 2);
        assert Arrays.stream(figure.points).anyMatch(it -> it.X == 2 && it.Y == 3);
        assert Arrays.stream(figure.points).anyMatch(it -> it.X == 4 && it.Y == 2);

        assert !figure.tryMoveFigure(5,5, 0, 4, 0, 3);
    }
}