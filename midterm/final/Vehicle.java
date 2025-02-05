// Name: Hsiang-Yu Lee, AndrewID: hsiangyl //
package finals;

public class Vehicle implements Comparable<Vehicle> {
    public static volatile int vehicleCount = 0; // Shared counter
    protected final int id;
    protected final int priority;

    public Vehicle(int priority) {
        this.id = ++vehicleCount;
        this.priority = priority;
    }

    @Override
    public int compareTo(Vehicle other) {
        if (this.priority != other.priority) {
            return Integer.compare(other.priority, this.priority); // Higher priority first
        }
        return Integer.compare(this.id, other.id); // Lower ID first for same priority
    }
}

