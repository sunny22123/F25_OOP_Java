// Name: Hsiang-Yu Lee, AndrewID: hsiangyl

package lab9;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestJavaCourse {

    @Test
    public void testSimulation() {
        // Parameters for the test
        final int NUM_TAS = 3;
        final int MAX_HELP_TIME = 200; // Maximum help time per TA

        // Create a TARoom and a JavaCourse instance
        TARoom taRoom = new TARoom();
        JavaCourse javaCourse = new JavaCourse(taRoom, NUM_TAS);

        // Run the simulation
        Thread courseThread = new Thread(javaCourse);
        courseThread.start();

        try {
            courseThread.join(); // Wait for the simulation to complete
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Assertions to validate the output
        int totalStudentsCreated = Student.getTotalStudentsCreated();
        int totalStudentsHelped = Student.getTotalStudentsHelped();
        int totalStudentsLeftInQueue = taRoom.getQueueLength();
        int maxQueueLength = javaCourse.getMaxQueueLength();
        int totalHelpTime = TA.getTotalHelpTime();

        // 1. Total students created equals students helped + students left in the queue
        assertEquals("Total students created does not match the sum of students helped and left in the queue",
                totalStudentsCreated, totalStudentsHelped + totalStudentsLeftInQueue);

        // 2. Total help time matches the sum of time spent by all TAs
        int calculatedTotalHelpTime = 0;
        for (TA ta : javaCourse.getTAs()) {
            calculatedTotalHelpTime += ta.getHelpTime();
        }
        assertEquals("Total help time does not match the sum of help time by all TAs",
                totalHelpTime, calculatedTotalHelpTime);

        // 3. Max help time is not exceeded
        for (TA ta : javaCourse.getTAs()) {
            assertTrue("A TA exceeded the maximum help time", ta.getHelpTime() <= MAX_HELP_TIME);
        }

        // 4. Max queue length is as expected
        assertTrue("Max queue length is invalid", maxQueueLength >= 0);

        // 5. AllDone flag is correctly set when total help time exceeds the limit
        assertTrue("AllDone flag was not correctly set", TA.isAllDone());
    }
}
