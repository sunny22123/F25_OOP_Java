//hsiangyulee, hsiangyl

package lab8;

import java.util.Queue;
import java.util.Random;

public class Kitchen implements Runnable {
    public static final int MEAL_RATE = 6;
    public static final int MAX_COOK_TIME = 7;
    public static final int MIN_COOK_TIME = 1;
    public static final int OPENING_STOCK = 175;
    public static final int MIN_STOCK = 4;

    private Queue<Guest> guestQ;
    private volatile int currentStock;
    private volatile boolean isUnderStock = false;
    private volatile boolean isOpen = true;

    private int guestsServed = 0;
    private int income = 0;

    public Kitchen(Queue<Guest> guestQ) {
        this.guestQ = guestQ;
        this.currentStock = OPENING_STOCK;
    }

    @Override
    public void run() {
        while (isOpen || !guestQ.isEmpty()) {
            Guest guest;
            synchronized (guestQ) {
                guest = guestQ.poll();
            }

            if (guest != null) {
                int meals = guest.placeOrder();
                if (currentStock >= meals) {
                    try {
                        Thread.sleep(meals * (new Random().nextInt(MAX_COOK_TIME - MIN_COOK_TIME + 1) + MIN_COOK_TIME));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    synchronized (this) {
                        currentStock -= meals;
                        income += meals * MEAL_RATE;
                        guestsServed++;
                    }
                } else {
                    synchronized (this) {
                        isUnderStock = true;
                        break;
                    }
                }
            }

            if (currentStock <= MIN_STOCK) {
                isUnderStock = true;
                break;
            }
        }
        isOpen = false;
    }

    public synchronized int getIncome() {
        return income;
    }

    public synchronized int getGuestsServed() {
        return guestsServed;
    }

    public synchronized int getCurrentStock() {
        return currentStock;
    }

    public boolean isUnderStock() {
        return isUnderStock;
    }

    public void closeKitchen() {
        isOpen = false;
    }
}
