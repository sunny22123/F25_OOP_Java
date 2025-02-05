// Name: Hsiang-Yu Lee, AndrewID: hsiangyl //
package finals;

import java.util.Random;

public class Traffic implements Runnable {
    private final TrafficResource resource;
    static final int MIN_VEHICLE_DELAY = 5, MAX_VEHICLE_DELAY = 10;
    private final Random random = new Random();

    public Traffic(TrafficResource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        while (Vehicle.vehicleCount < Road.maxVehicles) {
            try {
                Thread.sleep(random.nextInt(MAX_VEHICLE_DELAY - MIN_VEHICLE_DELAY + 1) + MIN_VEHICLE_DELAY);
                Vehicle vehicle = random.nextDouble() < 0.25 ? new EmergencyVehicle() : new RegularVehicle();
                resource.joinVehicle(vehicle);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}