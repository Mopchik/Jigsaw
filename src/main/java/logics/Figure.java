package logics;

import java.util.Random;

public class Figure {

    public Point[] points;
    public void moveFigure(int xMove, int yMove){
        for(int i = 0; i < points.length; i++){
            points[i].X += xMove;
            points[i].Y += yMove;
        }
    }

    public static TypeOfFigure getByInt(int num){
        return TypeOfFigure.values()[num];
    }

    /**
     * Sets to the figure new position if it is possible with bounds given.
     * @param xMove Vector.X of movement
     * @param yMove Vector.Y of movement
     * @param xMin Lower x bound of the field we're trying to move in.
     * @param xMax Upper x bound of the field we're trying to move in.
     * @param yMin Lower y bound of the field we're trying to move in.
     * @param yMax Upper y bound of the field we're trying to move in.
     * @return False if new figure position is out of bounds.
     */
    public boolean tryMoveFigure(int xMove, int yMove,
                                 int xMin, int xMax, int yMin, int yMax){
        int xleftOfFig = points[0].X;
        int xrightOfFig = points[0].X;
        int ybotOfFig = points[0].Y;
        int ytopOfFig = points[0].Y;
        for(int i = 0; i < points.length; i++){
            if(points[i].X < xleftOfFig)
                xleftOfFig = points[i].X;
            if(points[i].X > xrightOfFig)
                xrightOfFig = points[i].X;
            if(points[i].Y < ybotOfFig)
                ybotOfFig = points[i].Y;
            if(points[i].Y > ytopOfFig)
                ytopOfFig = points[i].Y;
        }
        if(xleftOfFig + xMove < xMin || xrightOfFig + xMove >= xMax ||
                ybotOfFig + yMove < yMin || ytopOfFig + yMove >= yMax)
            return false;
        moveFigure(xMove, yMove);
        return true;
    }

    public enum TypeOfFigure{
        aa, ab, ac, ad,
        ba, bb, bc, bd,
        ca, cb, cc, cd,
        da, db, dc, dd,
        ea, eb, ec, ed,
        fa, fb,
        g,
        ha, hb, hc, hd,
        ia, ib, ic, id
    }


    public static Figure createRandomFigure(){
        var type = createRandomType();
        return new Figure(type);
    }

    /**
     * It is more comfortable to store and send types instead of figures.
     * @return
     */
    public static TypeOfFigure createRandomType(){
        Random random = new Random();
        TypeOfFigure type = TypeOfFigure.values()[random.nextInt(31)];
        return type;
    }

    /**
     * There are many types of figure and this method shows how each type
     * looks on the field 3x3.
     * @param type
     */
    public Figure(TypeOfFigure type){
        Point one = new Point(0, 0);
        Point two = new Point(0, 1);
        Point three = new Point(0, 2);
        Point four = new Point(1, 0);
        Point five = new Point(1, 1);
        Point six = new Point(1, 2);
        Point seven = new Point(2, 0);
        Point eight = new Point(2, 1);
        Point nine = new Point(2, 2);
        switch (type){
            case aa:
                points = new Point[]{one, two, four, seven};
                break;
            case ab:
                points = new Point[]{one, four, five, six};
                break;
            case ac:
                points = new Point[]{seven, two, five, eight};
                break;
            case ad:
                points = new Point[]{one, two, three, six};
                break;
            case ba:
                points = new Point[]{one, two, five, eight};
                break;
            case bb:
                points = new Point[]{three, four, five, six};
                break;
            case bc:
                points = new Point[]{one, four, seven, eight};
                break;
            case bd:
                points = new Point[]{one, two, three, four};
                break;
            case ca:
                points = new Point[]{one, five, four, eight};
                break;
            case cb:
                points = new Point[]{two, three, four, five};
                break;
            case cc:
                points = new Point[]{five, two, four, seven};
                break;
            case cd:
                points = new Point[]{one, two, five, six};
                break;
            case da:
                points = new Point[]{three, six, eight, seven, nine};
                break;
            case db:
                points = new Point[]{one, four, eight, seven, nine};
                break;
            case dc:
                points = new Point[]{one, two, four, seven, three};
                break;
            case dd:
                points = new Point[]{one, two, six, nine, three};
                break;
            case ea:
                points = new Point[]{five, two, eight, seven, nine};
                break;
            case eb:
                points = new Point[]{five, two, eight, one, three};
                break;
            case ec:
                points = new Point[]{five, six, four, seven, one};
                break;
            case ed:
                points = new Point[]{five, six, four, nine, three};
                break;
            case fa:
                points = new Point[]{one, two, three};
                break;
            case fb:
                points = new Point[]{one, four, seven};
                break;
            case g:
                points = new Point[]{one};
                break;
            case ha:
                points = new Point[]{one, two, four};
                break;
            case hb:
                points = new Point[]{one, two, five};
                break;
            case hc:
                points = new Point[]{two, four, five};
                break;
            case hd:
                points = new Point[]{one, four, five};
                break;
            case ia:
                points = new Point[]{one, five, four, seven};
                break;
            case ib:
                points = new Point[]{one, two, three, five};
                break;
            case ic:
                points = new Point[]{two, four, five, eight};
                break;
            case id:
                points = new Point[]{two, four, five, six};
                break;
        }
    }
}
