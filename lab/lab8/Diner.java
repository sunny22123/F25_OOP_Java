//hsiangyulee, hsiangyl
package lab8;

import java.util.Queue;
import java.util.LinkedList;

public class Diner implements Runnable {
    public static final int GUEST_DELAY = 30;
    public static final int DINER_DURATION = 1000;

    private volatile boolean isOpen = true;
    public long startTime;
    public long endTime;

    public Kitchen kitchen;
    private Queue<Guest> guestQ;

    public int guestsEntered = 0;
    public int maxQLength = 0;

    public Diner() {
        this.guestQ = new LinkedList<>();
        this.kitchen = new Kitchen(guestQ);
    }

    @Override
    public void run() {
        startTime = System.currentTimeMillis();
        Thread kitchenThread = new Thread(kitchen);
        kitchenThread.start();

        while (System.currentTimeMillis() - startTime < DINER_DURATION) {
            try {
                Thread.sleep(GUEST_DELAY);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            Guest guest = new Guest();
            synchronized (guestQ) {
                guestQ.add(guest);
                guestsEntered++;
                maxQLength = Math.max(maxQLength, guestQ.size());
            }
        }

        endTime = System.currentTimeMillis();
        isOpen = false;
        kitchen.closeKitchen();

        try {
            kitchenThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        printReport();
    }

    private void printReport() {
        System.out.println("-------------- Guests --------------");
        System.out.println("Total guests entered: " + guestsEntered);
        System.out.println("Total guests served: " + kitchen.getGuestsServed());
        System.out.println("Guests declined service: " + (guestsEntered - kitchen.getGuestsServed()));
        System.out.println("-------------- Kitchen -------------");
        System.out.println("Meals left: " + kitchen.getCurrentStock());
        System.out.println("Closing status: " + (kitchen.isUnderStock() ? "Under stock" : "Overstock"));
        System.out.println("-------------- Diner ---------------");
        System.out.println("Max Q length: " + maxQLength);
        System.out.println("Diner was open for: " + (endTime - startTime) + " ms");
        System.out.println("Income: $" + kitchen.getIncome());
    }

    public static void main(String[] args) {
        Diner diner = new Diner();
        Thread dinerThread = new Thread(diner);
        dinerThread.start();

        try {
            dinerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

