//hsiangyulee, hsiangyl
package lab8;

import java.util.Random;

public class Guest {
    public static final int MAX_MEALS = 4;

    public int placeOrder() {
        return new Random().nextInt(MAX_MEALS) + 1;
    }
}


