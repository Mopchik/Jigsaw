package base;

import java.io.IOException;
import java.sql.*;

/**
IMPORTANT!! To use this class first you need to run
"runServerStart.bat" on the server. After using the base
run "runServerShutdown" to close the connection.
This is a class for writing results to the base and
getting top10 results from it. When first running
the jigsaw game it will create the base for results.
 */
public class ClientBaseManager {
    private Statement stat;
    private Connection connection;
    private static final String tableName = "WINNERS";
    private PreparedStatement psInsert;

    /**
    Creates ClientBaseManager object that allows you to send results
    and get data from the base from any class by connection.
     */
    public ClientBaseManager(String host, int port) throws SQLException, IOException {
        String url = "jdbc:derby://" + host + ":" + port + "/Tables;create=true";
        connection = getConnection(url);
        stat = connection.createStatement();
        createTableIfNull();
        psInsert = connection.prepareStatement("insert into " + tableName +
                "(LOGIN, STEPS, TIME) values (?, ?, ?)");
    }

    /**
     *
     * @return Top 10 best results as a String separated by newLine.
     */
    public String getTableResults(){
        ResultSet result = null;
        StringBuilder strb = new StringBuilder();
        try {
            result = stat.executeQuery(
                    "select ENTRY_DATE, LOGIN, STEPS, TIME from WINNERS order by STEPS DESC, TIME ASC, ENTRY_DATE ASC");
            int count = 0;
            while (result.next() && count < 10) {
                count++;
                strb.append(count).append(". ")
                        .append(result.getString(2))
                        .append(": Steps - ").append(result.getInt(3))
                        .append("; Time - ").append(result.getString(4))
                        .append("; Date - ").append(result.getTimestamp(1))
                        .append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return strb.toString();
    }

    /**
     * Writes new result to table.
     * @param login Name of the player.
     * @param steps Steps in Jigsaw game.
     * @param time Time of the game.
     * @throws SQLException Error in inserting.
     */
    public void writeToTable(String login, int steps, String time) throws SQLException {
        psInsert.setString(1,login);
        psInsert.setInt(2, steps);
        psInsert.setString(3, time);
        psInsert.executeUpdate();
    }

    /**
     * Creates table when it is used first time.
     * @throws SQLException
     */
    private void createTableIfNull() throws SQLException{
        if(tableExists()) return;
        stat.executeUpdate("CREATE TABLE " + tableName
                +  "(WINNERS_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY "
                +  "   CONSTRAINT WINNERS_PK PRIMARY KEY, "
                +  " ENTRY_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                +  " LOGIN CHAR(20), STEPS INT, TIME CHAR(12))");
    }

    /**
     *
     * @return
     * @throws SQLException Some problems with the base connection.
     */
    private boolean tableExists() throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet resultSet = meta.getTables(null, null, tableName, new String[] {"TABLE"});
        return resultSet.next();
    }

    public static Connection getConnection(String url) throws SQLException {
        String drivers = "org.apache.derby.client.ClientAutoloadedDriver";
        System.setProperty("jdbc.drivers", drivers);
        return DriverManager.getConnection(url);
    }

    public void close() throws SQLException {
        connection.close();
    }
}
