//hsiangyulee, hsiangyl
package lab8;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestDynamicDiner {

    private static Diner diner;  // Class-level variables to store simulation results
    private static Kitchen kitchen;
    private static long dinerDuration;

    @BeforeAll
    public static void setupSimulation() {
        diner = new Diner();
        Thread dinerThread = new Thread(diner);
        dinerThread.start();

        try {
            dinerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Save simulation results for assertions
        kitchen = diner.kitchen;
        dinerDuration = diner.endTime - diner.startTime;
    }

    @Test
    public void testDinerDuration() {
        // Assert that diner ran for at least DINER_DURATION if understock didn't occur
        assertTrue(dinerDuration >= Diner.DINER_DURATION || kitchen.isUnderStock(),
                "Diner did not run for the required duration or understock occurred unexpectedly.");
    }

    @Test
    public void testKitchenStock() {
        // Assert that the kitchen stock is <= MIN_STOCK if understock occurred
        assertTrue(kitchen.getCurrentStock() <= Kitchen.MIN_STOCK || !kitchen.isUnderStock(),
                "Kitchen stock is incorrect or understock flag is wrong.");
    }

    @Test
    public void testUnderstockFlag() {
        // Assert that understock flag is correctly set based on duration
        assertEquals(kitchen.isUnderStock(), dinerDuration < Diner.DINER_DURATION,
                "Understock flag does not match simulation duration.");
    }

    @Test
    public void testGuestsServedAndIncome() {
        // Assert that total income and guests served are consistent with expected values
        assertTrue(kitchen.getGuestsServed() > 0, "No guests were served.");
        assertTrue(kitchen.getIncome() > 0, "Income should be greater than zero.");
    }

    @Test
    public void testMaxQueueLength() {
        // Assert that the maximum queue length is correctly recorded
        assertTrue(diner.maxQLength > 0, "Maximum queue length was not recorded correctly.");
    }
}
