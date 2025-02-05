//hsiangyl, leehsiangyu
package exam2;

public class Caesar extends Message {

    @Override
    public String encrypt() {
        int shift = Integer.parseInt(key);
        StringBuilder encrypted = new StringBuilder();

        for (char c : message.toCharArray()) {
            if (c == SPACE) {
                encrypted.append(c);
            } else {
                char shifted = (char) ('a' + (c - 'a' + shift) % 26);
                encrypted.append(shifted);
            }
        }
        return encrypted.toString();
    }

    @Override
    public String decrypt() {
        int shift = Integer.parseInt(key);
        StringBuilder decrypted = new StringBuilder();

        for (char c : message.toCharArray()) {
            if (c == SPACE) {
                decrypted.append(c);
            } else {
                char shifted = (char) ('a' + (c - 'a' - shift + 26) % 26);
                decrypted.append(shifted);
            }
        }
        return decrypted.toString();
    }
}
