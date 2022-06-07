package graphics;

import logics.Field;
import logics.Figure;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.util.ArrayList;

/**
 * This class creates small 9*9 field in the top right angle of the frame.
 */
public class GeneratedSmallField extends JPanel implements DragGestureListener {
    Field field;
    private int SMP_WIDTH;
    private int SMP_HEIGHT;
    private int stepper;
    ArrayList<Figure.TypeOfFigure> fgs;
    boolean hasFgs = false;

    public GeneratedSmallField(int x, int y, int width, int height) {
        this.SMP_WIDTH = width;
        this.SMP_HEIGHT = height;
        setBounds(x, y, width, height);
        initUI();
    }

    public GeneratedSmallField(int x, int y, int width, int height, ArrayList<Figure.TypeOfFigure> fgs) {
        this.SMP_WIDTH = width;
        this.SMP_HEIGHT = height;
        setBounds(x, y, width, height);
        this.fgs = fgs;
        hasFgs= true;
        initUI();
    }

    private ArrayList<Rectangle> squares = new ArrayList<>();
    private void initUI() {
        field = new Field(3, 3);
        add();
        stepper = 1;
        var ds = new DragSource();
        ds.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE,this);
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++) {
                squares.add(new Rectangle(i * (SMP_WIDTH/3),j * (SMP_HEIGHT/3), SMP_WIDTH /3, SMP_HEIGHT /3));
            }
        }
    }

    public void updateFigures(ArrayList<Figure.TypeOfFigure> newFigs){
        fgs = newFigs;
        hasFgs = true;
    }

    private void add(){
        if(hasFgs){
            field.tryAddNewFigure(new Figure(fgs.get(stepper)));
        } else{
            field.tryAddNewFigure(Figure.createRandomFigure());
        }
    }

    public void updateFigure(){
        field.deleteFigure(0);
        add();
        stepper++;
    }

    public void refresh(){
        stepper = 0;
        add();
        stepper = 1;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        boolean[][] arrOfCellAvailability = field.getArrOfAvailability();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++) {
                Color color;
                if (arrOfCellAvailability[i][j])
                    color = Color.WHITE;
                else
                    color = Color.BLUE;
                g2.setColor(color);
                g2.fill(squares.get(3*i+j));
            }
        }
        g2.setColor(Color.BLACK);
        for(var sq:squares){
            g2.draw(sq);
        }
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void clear(){
        field.clear();
        //updateField();
    }

    public int steps(){
        return  stepper;
    }
    //public static void main(String[] args, DropAcceptor drc) {
    //    EventQueue.invokeLater(() -> new MyFrame(drc).setVisible(true));
    //}

    // DragGestureListener:
    @Override
    public void dragGestureRecognized(DragGestureEvent dge) {
        var cursor = Cursor.getDefaultCursor();
        if (dge.getDragAction() == DnDConstants.ACTION_MOVE) {
            cursor = DragSource.DefaultMoveDrop;
        }
        dge.startDrag(cursor, new TransferablePanel(field.getFigure(0)));
    }
}

/**
 * This class responds for listening mouse click to the panel for the drugging.
 */
class TransferablePanel implements Transferable {
    protected static final DataFlavor panelFlavor =
            new DataFlavor(Figure.class, "A JPanel Object");
    protected static final DataFlavor[] supportedFlavors = {panelFlavor};

    private final Figure figure;

    TransferablePanel(Figure figure) {
        this.figure = figure;
    }
    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return supportedFlavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(panelFlavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        if (flavor.equals(panelFlavor)) {
            return figure;
        }  else
            throw new UnsupportedFlavorException(flavor);
    }
}

