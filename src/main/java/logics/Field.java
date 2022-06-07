package logics;

import java.util.ArrayList;

public class Field {
    private int xSize, ySize;
    private ArrayList<Figure> figures;

    /**
     *
     * @param xSize Num of horizontal cells.
     * @param ySize Num of vertical cells.
     */
    public Field(int xSize, int ySize){
        this.xSize = xSize;
        this.ySize = ySize;
        figures = new ArrayList<Figure>();
    }

    /**
     * Adds figure to the field.
     * @param figure
     * @return False if figure's coordinates are out of bounds of the field.
     */
    public boolean tryAddNewFigure(Figure figure){
        for(int i = 0; i < figure.points.length; i++){
            if(!checkPointAvailable(figure.points[i].X, figure.points[i].Y))
                return false;
        }
        figures.add(figure);
        return true;
    }

    public void deleteFigure(int ind){
        if(ind >= 0 && ind < figures.size()){
            figures.remove(ind);
        }
    }
    public Figure getFigure(int ind){
        if(ind >= 0 && ind < figures.size()){
            return figures.get(ind);
        }
        System.out.println("Error");
        return null;
    }

    /**
     *
     * @param x
     * @param y
     * @return False if point is out of bounds of the field.
     */
    public boolean checkPointAvailable(int x, int y){
        for(int i = 0; i < figures.size(); i++){
            for(int j = 0; j < figures.get(i).points.length; j++){
                if(figures.get(i).points[j].X == x && figures.get(i).points[j].Y == y)
                    return false;
            }
        }
        return true;
    }

    /**
     *
     * @return Array [xSize,ySize] of booleans: is point(x,y) available?
     */
    public boolean[][] getArrOfAvailability(){
        boolean[][] arr = new boolean[9][9];
        for(int i = 0; i < xSize; i++){
            for(int j = 0; j < ySize; j++){
                arr[i][j] = checkPointAvailable(i, j);
            }
        }
        return arr;
    }

    public boolean contains(Figure f){
        return figures.contains(f);
    }

    public int size(){
        return figures.size();
    }
    public void clear(){figures.clear();}
}
