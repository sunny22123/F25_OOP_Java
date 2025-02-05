//hsiangyl, hsiang yu lee
package lab3;

public class Fraction {
    protected int numerator;
    protected int denominator;

    // Default constructor: sets numerator and denominator to 1
    public Fraction() {
        this.numerator = 1;
        this.denominator = 1;
    }

    // Non-default constructor: initializes with passed values and handles zero denominator
    public Fraction(int numerator, int denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("Denominator cannot be zero.");
        }
        this.numerator = numerator;
        this.denominator = denominator;
        reduce();  // Reduce the fraction to its simplest form
    }

    // Converts the fraction to its decimal equivalent
    public double toDecimal() {
        if (denominator == 0) {
            return Double.NaN; // Undefined (0/0 or division by zero)
        }
        return (double) numerator / denominator;
    }

    // Returns the fraction as a string in the format "numerator/denominator"
    @Override
    public String toString() {
        if (denominator == 0) {
            return "Undefined (Denominator is zero)";
        } else if (numerator == 0) {
            return "0";
        }
        return numerator + "/" + denominator;
    }

    // Adds two fractions and returns the result as a reduced fraction
    public Fraction add(Fraction other) {
        int newNumerator = this.numerator * other.denominator + other.numerator * this.denominator;
        int newDenominator = this.denominator * other.denominator;
        return new Fraction(newNumerator, newDenominator);
    }

    // Reduces the fraction to its simplest form
    private void reduce() {
        int gcd = findGCD(numerator, denominator);
        this.numerator /= gcd;
        this.denominator /= gcd;
    }

    // Finds the greatest common divisor (GCD) using recursion
    public int findGCD(int n, int d) {
        if (n == 0) {
            return 1;
        }
        if (d == 0) {
            return n;
        }
        return findGCD(d, n % d);
    }
}






