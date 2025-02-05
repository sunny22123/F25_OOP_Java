//hsiangyulee, hsiangyl
package lab7;

public class Nomination implements Comparable<Nomination> {
    private String category;
    private String title;
    private String artist;

    public Nomination(String category, String title, String artist) {
        this.category = category;
        this.title = title;
        this.artist = artist;
    }

    public String getCategory() { return category; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }

    @Override
    public String toString() {
        return String.format("%s: %s, %s", artist, title, category);
    }

    @Override
    public int compareTo(Nomination other) {
        return this.artist.compareToIgnoreCase(other.artist);
    }
}