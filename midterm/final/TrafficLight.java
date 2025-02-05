// Name: Hsiang-Yu Lee, AndrewID: hsiangyl //
package finals;

public class TrafficLight implements Runnable {
    private final TrafficResource resource;
    static final int TRAFFIC_LIGHT_DELAY = 100;

    public TrafficLight(TrafficResource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        while (Vehicle.vehicleCount < Road.maxVehicles) {
            try {
                Thread.sleep(TRAFFIC_LIGHT_DELAY);
                resource.changeLight();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
