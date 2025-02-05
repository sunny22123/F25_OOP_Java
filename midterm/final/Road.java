// Name: Hsiang-Yu Lee, AndrewID: hsiangyl //
package finals;

public class Road implements Runnable {
    public static int maxVehicles;
    public static int totalVehiclesPassed = 0;
    public static int emergencyVehiclesPassed = 0;
    public static int totalConsumerLoopExecution = 0;

    private final TrafficResource resource = new TrafficResource();

    public void startRoad() {
        System.out.print("How many vehicles? ");
        maxVehicles = new java.util.Scanner(System.in).nextInt();

        Thread producer = new Thread(new Traffic(resource));
        Thread trafficLight = new Thread(new TrafficLight(resource));
        Thread consumer = new Thread(this);

        producer.start();
        trafficLight.start();
        consumer.start();

        try {
            producer.join();
            trafficLight.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        while (totalVehiclesPassed < maxVehicles) {
            resource.releaseVehicle();
        }
    }

    public void printReport() {
        System.out.println("-----------TRAFFIC REPORT---------------------------");
        System.out.printf("The program ran for %d ms%n", TrafficLight.TRAFFIC_LIGHT_DELAY * maxVehicles);
        System.out.printf("Max Q length at traffic light was %d%n", resource.maxQLength);
        System.out.printf("Final Q length at traffic light was %d%n", resource.vehicleQ.size());
        System.out.printf("Total Vehicles passed: %d%n", totalVehiclesPassed);
        System.out.printf("Emergency Vehicles passed: %d%n", emergencyVehiclesPassed);
        System.out.printf("Number of consumer thread loop execution: %d%n", totalConsumerLoopExecution);
    }

    void checkAssertions() {
        assert maxVehicles == totalVehiclesPassed + resource.vehicleQ.size();
        assert resource.maxQLength >= resource.vehicleQ.size();
        assert Vehicle.vehicleCount == maxVehicles;
        assert totalConsumerLoopExecution == totalVehiclesPassed;
    }

    public static void main(String[] args) {
        Road road = new Road();
        road.startRoad();
        road.printReport();
        road.checkAssertions();
    }
}
