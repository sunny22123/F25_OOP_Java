/**
 * This class represents the data model for a case.
 * It stores details about the case including date, title, type, case number, category, and notes.
 * The Case class supports comparisons based on the case date.
 *
 * @author Hsiang-Yu Lee / hsiangyl
 */
package HW3;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Case implements Comparable<Case> {
    private final StringProperty caseDate;
    private final StringProperty caseTitle;
    private final StringProperty caseType;
    private final StringProperty caseNumber;
    private final StringProperty caseLink;
    private final StringProperty caseCategory;
    private final StringProperty caseNotes;

    // Default constructor
    public Case() {
        this.caseDate = new SimpleStringProperty("");
        this.caseTitle = new SimpleStringProperty("");
        this.caseType = new SimpleStringProperty("");
        this.caseNumber = new SimpleStringProperty("");
        this.caseLink = new SimpleStringProperty("");
        this.caseCategory = new SimpleStringProperty("");
        this.caseNotes = new SimpleStringProperty("");
    }

    public Case(String caseDate, String title, String caseType, String caseNumber,
                String caseLink, String category, String caseNotes) {
        this.caseDate = new SimpleStringProperty(caseDate);
        this.caseTitle = new SimpleStringProperty(title);
        this.caseType = new SimpleStringProperty(caseType);
        this.caseNumber = new SimpleStringProperty(caseNumber);
        this.caseLink = new SimpleStringProperty(caseLink);
        this.caseCategory = new SimpleStringProperty(category);
        this.caseNotes = new SimpleStringProperty(caseNotes);
    }

    // Getters and Setters
    public String getCaseDate() { return caseDate.get(); }
    public void setCaseDate(String caseDate) { this.caseDate.set(caseDate); }

    public String getCaseTitle() { return caseTitle.get(); }
    public void setCaseTitle(String caseTitle) { this.caseTitle.set(caseTitle); }

    public String getCaseType() { return caseType.get(); }
    public void setCaseType(String caseType) { this.caseType.set(caseType); }

    public String getCaseNumber() { return caseNumber.get(); }
    public void setCaseNumber(String caseNumber) { this.caseNumber.set(caseNumber); }

    public String getCaseLink() { return caseLink.get(); }
    public void setCaseLink(String caseLink) { this.caseLink.set(caseLink); }

    public String getCategory() { return caseCategory.get(); }
    public void setCategory(String category) { this.caseCategory.set(category); }

    public String getCaseNotes() { return caseNotes.get(); }
    public void setCaseNotes(String caseNotes) { this.caseNotes.set(caseNotes); }

    @Override
    public String toString() {
        return String.format("%s: %s", caseNumber.get(), caseTitle.get());
    }

    @Override
    public int compareTo(Case other) {
        if (this.caseDate.get() == null && other.caseDate.get() == null) return 0;
        if (this.caseDate.get() == null) return 1;
        if (other.caseDate.get() == null) return -1;
        return other.caseDate.get().compareTo(this.caseDate.get()); // Reverse comparison for descending order
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Case other = (Case) o;
        return caseNumber.get() != null && caseNumber.get().equals(other.caseNumber.get());
    }

    @Override
    public int hashCode() {
        return caseNumber.get() != null ? caseNumber.get().hashCode() : 0;
    }
}
