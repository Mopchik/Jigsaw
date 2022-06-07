package others;

import logics.Figure;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RequestObjectConverterTest {

    @Test
    void readFigures() {
        ArrayList<Figure.TypeOfFigure> figs = new ArrayList<>();
        figs.add(Figure.TypeOfFigure.aa);
        figs.add(Figure.TypeOfFigure.ab);
        figs.add(Figure.TypeOfFigure.ad);
        String figsStr = RequestObjectConverter.convertFiguresToRequest(figs);
        figsStr = String.valueOf(Command.FIGURES)  + " " + figsStr;
        ArrayList<Figure.TypeOfFigure> readedFigs = RequestObjectConverter.readFigures(figsStr.split(" "));
        for(int i = 0; i < readedFigs.size() || i < figs.size(); i++){
            assert figs.get(i) == readedFigs.get(i);
        }
    }

    @Test
    void convertNamesToRequest() {
        ArrayList<String> names = new ArrayList<>();
        names.add("Kostya");
        names.add("Vanya");
        names.add("Edik");
        assert "Kostya Vanya Edik ".equals(RequestObjectConverter.convertNamesToRequest(names));
    }

    @Test
    void convertFiguresToRequest() {
        ArrayList<Figure.TypeOfFigure> figs = new ArrayList<>();
        figs.add(Figure.TypeOfFigure.aa);
        figs.add(Figure.TypeOfFigure.ab);
        figs.add(Figure.TypeOfFigure.ad);
        assert "0 1 3 ".equals(RequestObjectConverter.convertFiguresToRequest(figs));
    }
}