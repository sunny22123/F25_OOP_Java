// Name: Hsiang-Yu Lee, AndrewID: hsiangyl //

package lab9;

import java.util.*;
import java.util.concurrent.locks.*;

class TARoom {
    private final LinkedList<Student> studentQ = new LinkedList<>();
    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();

    public void putStudent(Student student) {
        lock.lock();
        try {
            studentQ.add(student);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public Student getStudent() {
        lock.lock();
        try {
            while (studentQ.isEmpty()) {
                notEmpty.await();
            }
            return studentQ.poll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            lock.unlock();
        }
    }

    public int getQueueLength() {
        lock.lock();
        try {
            return studentQ.size();
        } finally {
            lock.unlock();
        }
    }
}
