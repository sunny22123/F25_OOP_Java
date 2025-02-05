//hsiangyl, leehsiangyu
package exam2;

public abstract class Message implements Encryptable {
    protected String message;
    protected String key;

    public void setMessage(String message, String key) {
        this.message = message;
        this.key = key;
    }
}
