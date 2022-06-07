package graphics;

import logics.Field;
import logics.Figure;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * This class represents the big field 9*9 in the left top angle of the frame.
 */
public class JigsawBigField extends JPanel {

    int BP_WIDTH;
    int BP_HEIGHT;
    private Field field;
    private GeneratedSmallField smallPanel;

    public JigsawBigField(int x, int y, int width, int height, GeneratedSmallField smallField) {
        this.setBounds(x, y, width, height);
        BP_WIDTH = width;
        BP_HEIGHT = height;
        this.smallPanel = smallField;
        initUI();
    }

    private void initUI() {
        field = new Field(9, 9);

        new MyDropListener();

        showField();
    }

    public int steps(){
        return field.size();
    }
    private ArrayList<Rectangle> squares = new ArrayList<>();
    public void showField(){
        squares.clear();
        boolean[][] arrOfCellAvailability = field.getArrOfAvailability();
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++) {
                Color color;
                if (arrOfCellAvailability[i][j])
                    color = Color.WHITE;
                else
                    color = Color.BLUE;
                squares.add(new Rectangle(i * (BP_WIDTH/9),j * (BP_HEIGHT/9), BP_WIDTH /9, BP_HEIGHT /9));
            }
        }
        SwingUtilities.updateComponentTreeUI(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        boolean[][] arrOfCellAvailability = field.getArrOfAvailability();
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++) {
                Color color;
                if (arrOfCellAvailability[i][j])
                    color = Color.WHITE;
                else
                    color = Color.BLUE;
                g2.setColor(color);
                g2.fill(squares.get(9*i+j));
            }
        }
        g2.setColor(Color.BLACK);
        for(var sq:squares){
            g2.draw(sq);
        }
    }

    public void clear(){
        field.clear();
        showField();
    }

    private static class MyComponent extends JComponent{
        Color color;
        int width, height;
        int x, y;
        MyComponent(Color color, int i, int j, int width, int height){
            this.color = color;
            this.width = width;
            this.height = height;
            x = width * i;
            y = height * j;
        }
        @Override
        protected void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D) g;
            Rectangle2D rectangle = new Rectangle2D.Double(x,y, width, height);
            g2.setPaint(color);
            g2.fill(rectangle);
        }
    }

    /**
     * This class listens dropping mouse to the field add figure if it was
     * dropped from the small panel.
     */
    private class MyDropListener extends DropTargetAdapter {
        MyDropListener() {
            new DropTarget(JigsawBigField.this, DnDConstants.ACTION_COPY, this, true, null);
        }

        @Override
        public void drop(DropTargetDropEvent dtde) {
            try {
                var tr = dtde.getTransferable();
                if (dtde.isDataFlavorSupported(TransferablePanel.panelFlavor)) {
                    var figure = (Figure) tr.getTransferData(TransferablePanel.panelFlavor);
                    if(field.contains(figure))
                        return;
                    Point p = dtde.getLocation();
                    double stepX = BP_WIDTH / 9;
                    double stepY = BP_HEIGHT / 9;
                    double Xd = p.getX() / stepX;
                    double Yd = p.getY() / stepY;
                    int X = (int)Math.floor(Xd);
                    int Y = (int)Math.floor(Yd);

                    if(!figure.tryMoveFigure(X, Y, 0, 9, 0, 9)){
                        System.out.println("Wrong Move Figure");
                        return;
                    }
                    if(!field.tryAddNewFigure(figure)){
                        System.out.println("Wrong Add to Field");
                        figure.moveFigure(-X, -Y);
                        return;
                    }
                    JigsawBigField.this.showField();

                    dtde.dropComplete(true);
                    smallPanel.updateFigure();
                    smallPanel.setVisible(true);
                    return;
                }
                dtde.rejectDrop();
            } catch (Exception e) {
                dtde.rejectDrop();
            }
        }
    }
}
