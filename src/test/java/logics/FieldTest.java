package logics;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {

    @Test
    void tryAddNewFigure() {
        Field field = new Field(3,3 );
        assert field.tryAddNewFigure(new Figure(Figure.TypeOfFigure.aa));
        assert Arrays.stream(field.getFigure(0).points).anyMatch(it -> it.X == 0 && it.Y == 0);
        assert Arrays.stream(field.getFigure(0).points).anyMatch(it -> it.X == 1 && it.Y == 0);
        assert Arrays.stream(field.getFigure(0).points).anyMatch(it -> it.X == 0 && it.Y == 1);
        assert Arrays.stream(field.getFigure(0).points).anyMatch(it -> it.X == 2 && it.Y == 0);
        assert !field.tryAddNewFigure(new Figure(Figure.TypeOfFigure.ab));
    }

    @Test
    void checkPointAvailable() {
        Field field = new Field(3,3 );
        field.tryAddNewFigure(new Figure(Figure.TypeOfFigure.aa));
        assert !field.checkPointAvailable(0,0);
        assert !field.checkPointAvailable(1,0);
        assert !field.checkPointAvailable(0,1);
        assert !field.checkPointAvailable(2,0);
    }

    @Test
    void getArrOfAvailability() {
        Field field = new Field(3,3 );
        field.tryAddNewFigure(new Figure(Figure.TypeOfFigure.aa));
        boolean[][] arr = field.getArrOfAvailability();
        for(int i = 0; i <3; i++){
            for(int j = 0; j < 3; j++){
                if(i == 0 && j == 0 ||
                        i == 1 && j == 0 ||
                        i == 0 && j == 1 ||
                        i == 2 && j == 0){
                    assert !arr[i][j];
                } else{
                    assert arr[i][j];
                }
            }
        }
    }
}