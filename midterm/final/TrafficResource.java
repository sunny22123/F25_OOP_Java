// Name: Hsiang-Yu Lee, AndrewID: hsiangyl //
package finals;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TrafficResource {
    Queue<Vehicle> vehicleQ = new PriorityQueue<>();
    Lock trafficLock = new ReentrantLock();
    Condition vehicleAvailableInQueue = trafficLock.newCondition();
    Condition vehicleCanGo = trafficLock.newCondition();
    int maxQLength = 0;
    boolean isGreen = true; // Starts with green light

    void joinVehicle(Vehicle vehicle) {
        trafficLock.lock();
        try {
            vehicleQ.offer(vehicle);
            maxQLength = Math.max(maxQLength, vehicleQ.size());
            if (!isGreen) {
                System.out.printf("\tRED: %s %d in Q. Q length %d%n", vehicle.getClass().getSimpleName(), vehicle.id, vehicleQ.size());
            }
            vehicleAvailableInQueue.signal();
        } finally {
            trafficLock.unlock();
        }
    }

    void releaseVehicle() {
        trafficLock.lock();
        try {
            // Wait until a vehicle is available
            while (vehicleQ.isEmpty() && Vehicle.vehicleCount < Road.maxVehicles) {
                vehicleAvailableInQueue.await();
            }

            // Wait for the light to turn green or for the front vehicle to be an emergency vehicle
            while (!isGreen && !(vehicleQ.peek() instanceof EmergencyVehicle)) {
                vehicleCanGo.await();
            }

            if (!vehicleQ.isEmpty()) {
                Vehicle vehicle = vehicleQ.poll();
                if (vehicle instanceof EmergencyVehicle) {
                    System.out.printf("%s %s %d passed. Q length: %d%n",
                            isGreen ? "Green:" : "RED:", vehicle.getClass().getSimpleName(), vehicle.id, vehicleQ.size());
                    Road.emergencyVehiclesPassed++;
                } else {
                    System.out.printf("Green: %s %d passed. Q length: %d%n",
                            vehicle.getClass().getSimpleName(), vehicle.id, vehicleQ.size());
                }
                Road.totalVehiclesPassed++;
                Road.totalConsumerLoopExecution++;
            }

            vehicleCanGo.signal();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            trafficLock.unlock();
        }
    }

    void changeLight() {
        trafficLock.lock();
        try {
            isGreen = !isGreen;
            System.out.println("Traffic light changed to " + (isGreen ? "GREEN" : "RED"));
            vehicleCanGo.signalAll();
        } finally {
            trafficLock.unlock();
        }
    }
}
