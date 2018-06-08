import java.util.Random;

public class Prime {
    private static int[][] buf;
    private static int[] a;

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int seed = Integer.parseInt(args[1]);
//        int n = 10;
//        int seed = 321;
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < n*n*1000) {
            // warm up jit
            // warm up prime factor calculation
            largestPrimeFac(new Random().nextInt(100),
                    new Random().nextInt(100),
                    new Random().nextInt(100));
            // malloc and warm up gc
            int[] arr = new int[10000];
        }
        Random random = new Random(seed);
        a = new int[n];
        for (int i = 0; i < a.length; i++) {
            a[i] = random.nextInt(100);
        }
        buf = new int[n][n];

        System.out.println(gain(0, n - 1));
        System.out.print("steps:");
        printSteps(0, n - 1);
        System.out.println();
    }

    private static int largestPrimeFac(int x, int b, int c) {
        if (b < 10)
            x *= 10;
        else
            x *= 100;
        x += b;
        if (c < 10)
            x *= 10;
        else
            x *= 100;
        x += c;
//        return largestPrimeFac(x);
//    }
//
//    private static int largestPrimeFac(int x) {
        if (x < 2)
            return 0;
        while (x % 2 == 0)
            x /= 2;
        if (x == 1)
            return 2;
        int a = 3;
        while (a * a <= x) {
            if (x % a == 0)
                x /= a;
            else
                a += 2;
        }
        return x;
    }

    private static int gain(int from, int to) {
        if (to - from < 2)
            return 0;
        int maxGain = buf[from][to];
        if (maxGain != 0)
            return maxGain;
        for (int mid = from + 1; mid < to; mid++) {
            int curGain = gain(from, mid) + largestPrimeFac(a[from], a[mid], a[to]) + gain(mid, to);
            if (curGain > maxGain)
                maxGain = curGain;
        }
        buf[from][to] = maxGain;
        return maxGain;
    }

    private static void printSteps(int from, int to) {
        int maxMid = from + 1;
        int maxGain = -1;
        for (int mid = from + 1; mid < to; mid++) {
            int curGain = buf[from][mid] + largestPrimeFac(a[from], a[mid], a[to]) + buf[mid][to];
            if (curGain > maxGain) {
                maxMid = mid;
                maxGain = curGain;
            }
        }
        if (to - maxMid > 1)
            printSteps(maxMid, to);
        if (maxMid - from > 1)
            printSteps(from, maxMid);
        System.out.print(" " + (from + 1));
    }
}
