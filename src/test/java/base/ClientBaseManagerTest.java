package base;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IMPORTANT!! To test this class first run "runServerStart.bat".
 * Delete DERBY/Tables directory to clear the table.
 */
class ClientBaseManagerTest {

    @Test
    void writeToTable() {
        try {
            ClientBaseManager cmb = new ClientBaseManager("localhost", 1527);
            cmb.writeToTable("Test", 100, "0h:0m:22s");
            StringBuilder shouldBe = new StringBuilder();
            shouldBe.append(1).append(". ")
                    .append("Test                ")
                    .append(": Steps - ").append(100)
                    .append("; Time - ").append("0h:0m:22s");
            assert cmb.getTableResults().split("\n")[0].substring(0, shouldBe.length())
                    .equals(shouldBe.toString());
        } catch (SQLException e) {
            assert false;
        } catch (IOException e) {
            assert false;
        }
    }

    @Test
    void getTableResults() {
        try {
            ClientBaseManager cmb = new ClientBaseManager("localhost", 1527);
            cmb.writeToTable("Kostya", 100, "0h:0m:23s");
            cmb.writeToTable("Vanya", 99, "0h:0m:12s");
            cmb.writeToTable("Dasha", 98, "0h:0m:15s");
            cmb.writeToTable("Edik", 99, "0h:0m:12s");
            cmb.writeToTable("Test", 100, "0h:0m:22s");

            String testShouldBe = shouldBe(1, "Test", 100, "0h:0m:22s");
            String kostyaShouldBe = shouldBe(2, "Kostya", 100, "0h:0m:23s");
            String vanyaShouldBe = shouldBe(3, "Vanya", 99, "0h:0m:12s");
            String dashaShouldBe = shouldBe(5, "Dasha", 98, "0h:0m:15s");
            String edikShouldBe = shouldBe(4, "Edik", 99, "0h:0m:12s");

            assert cmb.getTableResults().split("\n")[0].substring(0, testShouldBe.length())
                    .equals(testShouldBe);
            assert cmb.getTableResults().split("\n")[1].substring(0, kostyaShouldBe.length())
                    .equals(kostyaShouldBe);
            assert cmb.getTableResults().split("\n")[2].substring(0, vanyaShouldBe.length())
                    .equals(vanyaShouldBe);
            assert cmb.getTableResults().split("\n")[3].substring(0, edikShouldBe.length())
                    .equals(edikShouldBe);
            assert cmb.getTableResults().split("\n")[4].substring(0, dashaShouldBe.length())
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