//hsiangyulee, hsiangyl
package lab7;

import java.util.List;

public class Artist implements Comparable<Artist> {
    private String name;
    public List<Nomination> nominations;

    public Artist(String name, List<Nomination> nominations) {
        this.name = name;
        this.nominations = nominations;
    }

    public String getName() { return name; }
    public List<Nomination> getNominations() { return nominations; }
    public int getNominationCount() { return nominations.size(); }

    @Override
    public String toString() {
        return String.format("%s: %d", name, getNominationCount());
    }

    @Override
    public int compareTo(Artist other) {
        // Sort by nomination count in descending order
        int countCompare = Integer.compare(other.getNominationCount(), this.getNominationCount());
        if (countCompare != 0) {
            return countCompare;
        }
        // If counts are equal, sort by name in ascending order
        return this.name.compareToIgnoreCase(other.name);
    }
}