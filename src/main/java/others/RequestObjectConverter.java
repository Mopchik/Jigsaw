package others;

import logics.Figure;

import java.util.ArrayList;

public class RequestObjectConverter {

    /**
     * Converts the request (String) to the Array of TypeOfFigure.
     * Is used for reading data from server by client.
     * @param strs
     * @return
     */
    public static ArrayList<Figure.TypeOfFigure> readFigures(String[] strs){
        ArrayList<Figure.TypeOfFigure> figs = new ArrayList<>();
        for(int i = 1; i < strs.length; i++){
            figs.add(Figure.getByInt(Integer.parseInt(strs[i])));
        }
        return figs;
    }

    /**
     * Converts the request (String) to the Array of client's names (String).
     * Is used for sending data from server to client.
     * @param names
     * @return
     */
    public static String convertNamesToRequest(ArrayList<String> names){
        StringBuilder strb = new StringBuilder();
        for(var n:names){
            strb.append(n);
            strb.append(" ");
        }
        return strb.toString();
    }

    /**
     * Converts the Array of TypeOfFigure to the request (String).
     * Is used for sending data from server to client.
     * @param figs
     * @return
     */
    public static String convertFiguresToRequest(ArrayList<Figure.TypeOfFigure> figs){
        StringBuilder strb = new StringBuilder();
        for(var f:figs){
            strb.append(f.ordinal());
            strb.append(" ");
        }
        return strb.toString();
    }

}
