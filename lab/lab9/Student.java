// Name: Hsiang-Yu Lee, AndrewID: hsiangyl //

package lab9;

import java.util.Random;

public class Student {
    private static int totalStudentsCreated = 0;
    private static int totalStudentsHelped = 0;
    private static final Random random = new Random();

    public static final int MIN_QUESTION_TIME = 1;
    public static final int MAX_QUESTION_TIME = 5;

    public final int studentID;

    public Student() {
        this.studentID = ++totalStudentsCreated;
    }

    public int askQuestion() {
        totalStudentsHelped++;
        return MIN_QUESTION_TIME + random.nextInt(MAX_QUESTION_TIME - MIN_QUESTION_TIME + 1);
    }

    public static int getTotalStudentsCreated() {
        return totalStudentsCreated;
    }

    public static int getTotalStudentsHelped() {
        return totalStudentsHelped;
    }
}