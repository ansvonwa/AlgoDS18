package javafoo;

import java.util.Random;

public class Input {
    public final int[] a;

    public Input(int n, int seed) {
        a = new int[n];
        Random random = new Random(seed);
        for (int i = 0; i < a.length; i++) {
            a[i] = random.nextInt(100);
        }
    }
}
