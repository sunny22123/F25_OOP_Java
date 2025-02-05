//hsiangyl, hsiang yu lee
package lab3;

public class MixedFraction extends Fraction {
    public int naturalNumber;

    // Non-default constructor with an additional natural number argument
    public MixedFraction(int naturalNumber, int numerator, int denominator) {
        super(numerator, (denominator == 0) ? 1 : denominator); // Ensure denominator is never zero
        this.naturalNumber = naturalNumber;

        // Custom handling for a denominator of 0
        if (denominator == 0) {
            System.out.println("Warning: Denominator cannot be zero. Defaulting to denominator 1.");
        }
    }

    // Overrides toString to display the mixed fraction as "N n/d"
    @Override
    public String toString() {
        if (denominator == 0) {
            return "Undefined (Denominator is zero)";
        } else if (numerator == 0) {
            return naturalNumber + "";
        }
        return naturalNumber + " " + numerator + "/" + denominator;
    }

    // Overrides toDecimal to calculate the decimal value of the mixed fraction
    @Override
    public double toDecimal() {
        if (denominator == 0) {
            return Double.NaN; // Undefined if denominator is zero
        }
        return naturalNumber + (double) numerator / denominator;
    }

    // Converts the mixed fraction to a regular fraction
    public Fraction toFraction() {
        if (denominator == 0) {
            return new Fraction(0, 1); // Handle undefined case with denominator defaulted to 1
        }
        int improperNumerator = (naturalNumber * denominator) + numerator;
        return new Fraction(improperNumerator, denominator);
    }

    // Overloaded method to add two mixed fractions
    public Fraction add(MixedFraction mf) {
        // Convert both mixed fractions to improper fractions and then add
        Fraction f1 = this.toFraction();
        Fraction f2 = mf.toFraction();
        return f1.add(f2);
    }
}



