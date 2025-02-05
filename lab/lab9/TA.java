// Name: Hsiang-Yu Lee, AndrewID: hsiangyl //

package lab9;

class TA implements Runnable, Comparable<TA> {
    private static int taCount = 0;
    private static int totalHelpTime = 0;
    private static boolean allDone = false; // Shared flag to indicate simulation should stop

    public final int taID;
    private int studentsHelped = 0;
    private int helpTime = 0;

    private final TARoom taRoom;

    public static final int MAX_HELP_TIME = 20; // Maximum time a TA can help

    public TA(TARoom taRoom) {
        this.taID = ++taCount;
        this.taRoom = taRoom;
    }

    @Override
    public void run() {
        while (!allDone && helpTime < MAX_HELP_TIME) {
            Student student = taRoom.getStudent();
            if (student == null) break;

            int questionTime = student.askQuestion();
            studentsHelped++;
            helpTime += questionTime;

            // Print student help details
            synchronized (System.out) {
                System.out.print(formatOutput(taID, "TA" + taID + ":" + "Student" + student.studentID + ":" + questionTime + "min"));
            }

            try {
                Thread.sleep(questionTime * 1000L); // Simulate question time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            synchronized (TA.class) {
                totalHelpTime += questionTime;
                if (totalHelpTime >= taCount * MAX_HELP_TIME && !allDone) {
                    allDone = true; // Set the allDone flag
                    synchronized (System.out) {
                        System.out.println("\n**************** All done flag set by TA" + taID + " ****************");
                    }
                }
            }
        }

        // Print TA done message
        synchronized (System.out) {
            System.out.print(formatOutput(taID, "---- TA" + taID + " done ----"));
        }
    }

    private String formatOutput(int taID, String message) {
        StringBuilder output = new StringBuilder();
        for (int i = 1; i < taID; i++) {
            output.append(String.format("%-25s", ""));
        }
        output.append(String.format("%-25s", message));
        output.append("\n");
        return output.toString();
    }

    public int getHelpTime() {
        return helpTime;
    }

    public int getStudentsHelped() {
        return studentsHelped;
    }

    public static boolean isAllDone() {
        return allDone;
    }

    public static int getTotalHelpTime() {
        return totalHelpTime;
    }

    @Override
    public int compareTo(TA other) {
        return Integer.compare(other.helpTime, this.helpTime);
    }
}



