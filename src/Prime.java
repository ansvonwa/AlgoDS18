import java.util.ArrayList;
import java.util.Random;

public class Prime {
    private static int[][] buf;
    public static int[] a;

    public static void main(String[] args) {
        int n = args.length > 0 ? Integer.parseInt(args[0]) : 10;
        int seed = args.length > 1 ? Integer.parseInt(args[1]) : 321;
        long start = System.currentTimeMillis();
        Random random = new Random(seed);
        a = new int[n];
        for (int i = 0; i < a.length; i++) {
            a[i] = random.nextInt(100);
        }
        buf = new int[n][n];

        System.out.println("bestGain = " + gain(0, n - 1));// 1k: 68782ms
//        System.out.println(gainIt());// 1k: 69232ms
        System.out.print("steps:");
        printSteps(0, n - 1);
        System.out.println();

        if (args.length == 0) {
            printStepsMin();
            System.out.println(System.currentTimeMillis() - start);
        }
    }

    public static void init(int[] a) {
        Prime.a = a;
        Prime.buf = new int[a.length][a.length];
    }

    public static int largestPrimeFac(int x, int b, int c) {
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

    public static int gainIt() {
        final int n = a.length;
        int[][] buf = new int[n][n];
        int[] a = Prime.a;
        for (int len = 2; len < n; len++) {
            for (int from = 0; from < n - len; from++) {
                final int to = from + len;
                final int aFrom = a[from];
                final int aTo = a[to];
                int maxGain = 0;
                for (int mid = from + 1; mid < to; mid++) {
                    int curGain = buf[from][mid] + largestPrimeFac(aFrom, a[mid], aTo) + buf[mid][to];
                    if (curGain > maxGain)
                        maxGain = curGain;
                }
                buf[from][to] = maxGain;
            }
        }
        Prime.buf = buf;
        return buf[0][n - 1];
    }

    public static int gain(int from, int to) {
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

    public static void printSteps(int from, int to) {
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

    public static void printStepsMin() {
        final int n = a.length;
        ArrayList<Integer> global = stepsGlobal(0, n - 1);
        System.out.print("steps:");
        for (int i = 0; i < global.size(); i++) {
            int shift = 0;
            for (int j = 0; j < i; j++)
                if (global.get(j) < global.get(i))
                    shift++;
            System.out.print(" " + (global.get(i) - shift));
        }
        System.out.println();
    }

    public static ArrayList<Integer> stepsGlobal(int from, int to) {
        int maxMid = from + 1;
        int maxGain = -1;
        for (int mid = from + 1; mid < to; mid++) {
            int curGain = buf[from][mid] + largestPrimeFac(a[from], a[mid], a[to]) + buf[mid][to];
            if (curGain > maxGain) {
                maxMid = mid;
                maxGain = curGain;
            }
        }
        ArrayList<Integer> steps = new ArrayList<>();
        if (maxMid - from > 1)
            steps.addAll(stepsGlobal(from, maxMid));
        if (to - maxMid > 1)
            steps.addAll(stepsGlobal(maxMid, to));
        steps.add(maxMid);
        return steps;
    }
}
