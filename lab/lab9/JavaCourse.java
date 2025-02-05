// Name: Hsiang-Yu Lee, AndrewID: hsiangyl //

package lab9;

import java.util.*;

public class JavaCourse implements Runnable {
    public static final int STUDENT_DELAY = 200; // Delay between student arrivals
    private final TARoom taRoom;
    private final List<TA> taList = new ArrayList<>();
    private final List<Thread> taThreads = new ArrayList<>();
    private int maxQLength = 0; // Tracks the maximum queue length

    public JavaCourse(TARoom taRoom, int numTAs) {
        this.taRoom = taRoom;
        for (int i = 0; i < numTAs; i++) {
            TA ta = new TA(taRoom);
            taList.add(ta);
            taThreads.add(new Thread(ta));
        }
    }

    public void run() {

        synchronized (System.out) {
            for (int i = 0; i < taList.size(); i++) {
                System.out.printf("%-25s", "TA" + (i + 1));
            }
            System.out.println(); // 換行
            System.out.println("-".repeat(25 * taList.size()));
        }

        long startTime = System.currentTimeMillis();


        taThreads.forEach(Thread::start);


        while (!TA.isAllDone()) {
            Student student = new Student();
            taRoom.putStudent(student);
            maxQLength = Math.max(maxQLength, taRoom.getQueueLength());

            try {
                Thread.sleep(STUDENT_DELAY);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }


        taThreads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        long endTime = System.currentTimeMillis();
        printReport(startTime, endTime);
    }

    // Getter for max queue length
    public int getMaxQueueLength() {
        return maxQLength;
    }

    // Getter for the list of TAs
    public List<TA> getTAs() {
        return taList;
    }

    private void printReport(long startTime, long endTime) {
        System.out.println("\n----------------------TAs----------------------");
        for (TA ta : taList) {
            System.out.printf("TA%d helped %d students for %d min\n", ta.taID, ta.getStudentsHelped(), ta.getHelpTime());
        }

        System.out.println("\n----------------------Time----------------------");
        System.out.printf("Total help time: %d min\n", TA.getTotalHelpTime());
        System.out.printf("Max help time: %d min\n", taList.size() * TA.MAX_HELP_TIME);
        System.out.printf("Total lapsed time: %d min\n", (endTime - startTime) / 600);

        System.out.println("\n--------------------Students--------------------");
        System.out.printf("Total students created: %d\n", Student.getTotalStudentsCreated());
        System.out.printf("Total students helped: %d\n", Student.getTotalStudentsHelped());
        System.out.printf("Max Q length: %d\n", maxQLength);
        System.out.printf("Students left in the Q: %d\n", taRoom.getQueueLength());
    }

    // Main method to start the simulation
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number of TAs: ");
        int numTAs = scanner.nextInt(); // Get the number of TAs from the user

        final TARoom taRoom = new TARoom(); // Shared resource for TAs and students

        // Initialize and start the JavaCourse simulation
        JavaCourse javaCourse = new JavaCourse(taRoom, numTAs);
        Thread courseThread = new Thread(javaCourse);

        System.out.println("Simulation started...");
        courseThread.start();

        try {
            courseThread.join(); // Wait for the simulation to complete
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Simulation ended.");
        scanner.close();
    }
}