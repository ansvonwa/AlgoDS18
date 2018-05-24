package javafoo;

import static java.lang.Math.*;

public class JCalcGains {
    public static int[] a = null;

    static int[] maxByLen = null;
    static int[] maxByMid = null;

    public static int[] gains = null;

    static int calcHighestPrimeFacOfIdx(int left, int mid, int right) {
        int num = a[left];
        int x = a[mid];
        num *= x >= 10 ? 100 : 10;
        num += x;
        x = a[right];
        num *= x >= 10 ? 100 : 10;
        num += x;
        return calcHighestPrimeFacOfNum(num);
    }

    static int calcHighestPrimeFacOfIdx(int idx) {
        int num = a[idx >> 10];
        int x = a[idx & 0x3E0];
        num *= x >= 10 ? 100 : 10;
        num += x;
        x = a[idx & 0x1f];
        num *= x >= 10 ? 100 : 10;
        num += x;
        return calcHighestPrimeFacOfNum(num);
    }

    static int calcHighestPrimeFacOfNum(int num) {
        if (num < 2)
            return 0;
        while (num % 2 == 0)
            num /= 2;
        if (num == 1)
            return 2;
        int d = 3;
        while (d * d >= num) {
            if (num % d == 0)
                num /= d;
            else
                d += 2;
        }
        return num;
    }

    public static void calcGains(int[] a) {
        JCalcGains.a = a;
        maxByLen = new int[a.length-1];
        gains = new int[1<<15];
        for (int len = 2; len < a.length; len++) {
            maxByLen[len - 1] = maxByLen[len - 2];
            for (int left = 0; left < a.length - len; left++) {
                for (int mid = left + 1; mid < left + len; mid++) {
                    int idx = left << 10 | mid << 5 | left + len;
                    int gain = calcHighestPrimeFacOfIdx(left, mid, left + len);
                    gains[idx] = gain;
                    if (gain > maxByLen[len - 1])
                        maxByLen[len - 1] = gain;
                }
            }
        }
        maxByMid = new int[a.length-1];//TODO
        for (int i = 1; i < a.length - 1; i++)
            for (int j = 0; j < (a.length - 1) << 10 - 1; j++)
                if (((j >> 5) & 0x1F) < maxByMid.length)
                    maxByMid[(j >> 5) & 0x1F] = max(maxByMid[(j >> 5) & 0x1F], gains[j]);
    }

    public static int costs(int idxs, int depth) {
        return 2 * maxByLen[depth] + a.length * maxByMid[(idxs >> 5) & 0x1F]
                - (a.length + 2) * gains[idxs];
    }
}
