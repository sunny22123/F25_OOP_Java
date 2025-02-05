//hsiangyl, leehsiangyu
package exam2;

public class Keyword extends Message {

    @Override
    public String encrypt() {
        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (c == SPACE) {
                encrypted.append(c);
            } else {
                int shift = key.charAt(i % key.length()) - 'a' + 1;
                char shifted = (char) ('a' + (c - 'a' + shift) % 26);
                encrypted.append(shifted);
            }
        }
        return encrypted.toString();
    }

    @Override
    public String decrypt() {
        StringBuilder decrypted = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (c == SPACE) {
                decrypted.append(c);
            } else {
                int shift = key.charAt(i % key.length()) - 'a' + 1;
                char shifted = (char) ('a' + (c - 'a' - shift + 26) % 26);
                decrypted.append(shifted);
            }
        }
        return decrypted.toString();
    }
}
