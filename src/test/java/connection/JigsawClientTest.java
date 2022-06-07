package connection;

import base.ClientBaseManager;
import org.junit.jupiter.api.Test;
import others.Command;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IMPORTANT!! To test this class first run "runServerStart.bat" and run Jigsaw server (JissawServerRun.main)
 * Delete DERBY/Tables directory to clear the table before you start testing.
 * These tests also check JigsawServer and JigsawServerThread.
 */
class JigsawClientTest {

    @Test
    void getRequest(){
        try{
            JigsawClient jc = new JigsawClient("localhost", 5000, "TestPlayer");
            assert String.valueOf(jc.getRequest().charAt(0)).equals(String.valueOf(Command.MAX_DURATION.ordinal()));
            assert jc.getRequest().equals(String.valueOf(Command.YOUR_NAME.ordinal()));
            jc.sendRequest(Command.YOUR_NAME, jc.getName());
            jc.sendRequest(Command.EXIT);
            jc.close();
        } catch (IOException e) {
            assert false;
        } catch (SQLException e) {
            assert false;
        }
    }

    @Test
    void sendRequest(){
        try {
            JigsawClient jc = new JigsawClient("localhost", 5000, "TestPlayer");
            assert String.valueOf(jc.getRequest().charAt(0)).equals(String.valueOf(Command.MAX_DURATION.ordinal()));
            assert jc.getRequest().equals(String.valueOf(Command.YOUR_NAME.ordinal()));
            jc.sendRequest(Command.YOUR_NAME, jc.getName());
            jc.sendRequest(Command.EXIT);
            assert jc.getRequest().equals(String.valueOf(Command.EXIT.ordinal()));
            jc.close();
        } catch (IOException e) {
            assert false;
        } catch (SQLException e) {
            assert false;
        }
    }

    @Test
    void writeToTable() {
        try {
            JigsawClient jc = new JigsawClient("localhost", 5000, "TestPlayer");
            jc.writeToTable("Test", 105, "0h:0m:22s");
            StringBuilder shouldBe = new StringBuilder();
            shouldBe.append(1).append(". ")
                    .append("Test                ")
                    .append(": Steps - ").append(105)
                    .append("; Time - ").append("0h:0m:22s");
            assert jc.getTableResults().split("\n")[0].substring(0, shouldBe.length())
                    .equals(shouldBe.toString());
        } catch (IOException e) {
            assert false;
        } catch (SQLException e) {
            assert false;
        }
    }

    @Test
    void getTableResults() {
        try {
            JigsawClient jc = new JigsawClient("localhost", 5000, "TestPlayer");
            jc.writeToTable("Kostya", 100, "0h:0m:23s");
            jc.writeToTable("Vanya", 99, "0h:0m:12s");
            jc.writeToTable("Dasha", 98, "0h:0m:15s");
            jc.writeToTable("Edik", 99, "0h:0m:12s");
            jc.writeToTable("Test", 100, "0h:0m:22s");

            String testShouldBe = shouldBe(1, "Test", 100, "0h:0m:22s");
            String kostyaShouldBe = shouldBe(2, "Kostya", 100, "0h:0m:23s");
            String vanyaShouldBe = shouldBe(3, "Vanya", 99, "0h:0m:12s");
            String dashaShouldBe = shouldBe(5, "Dasha", 98, "0h:0m:15s");
            String edikShouldBe = shouldBe(4, "Edik", 99, "0h:0m:12s");

            assert jc.getTableResults().split("\n")[0].substring(0, testShouldBe.length())
                    .equals(testShouldBe);
            assert jc.getTableResults().split("\n")[1].substring(0, kostyaShouldBe.length())
                    .equals(kostyaShouldBe);
            assert jc.getTableResults().split("\n")[2].substring(0, vanyaShouldBe.length())
                    .equals(vanyaShouldBe);
            assert jc.getTableResults().split("\n")[3].substring(0, edikShouldBe.length())
                    .equals(edikShouldBe);
            assert jc.getTableResults().split("\n")[4].substring(0, dashaShouldBe.length())
                    .equals(dashaShouldBe);

        } catch (SQLException e) {
            assert false;
        } catch (IOException e) {
            assert false;
        }
    }
    String shouldBe(int ind, String name, int steps, String time){
        while(name.length() < 20){
            name = name + " ";
        }
        StringBuilder testShouldBe = new StringBuilder();
        testShouldBe.append(ind).append(". ")
                .append(name)
                .append(": Steps - ").append(steps)
                .append("; Time - ").append(time);
        return testShouldBe.toString();
    }
}