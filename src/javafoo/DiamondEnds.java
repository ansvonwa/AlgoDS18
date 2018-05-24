package javafoo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DiamondEnds {

    static int len = -1;
    static HashMap<Long, Integer>[] fwdLists = null;


    public static void exploreAllFwd() {
        fwdLists = new HashMap[len];
        for (int i = 0; i < fwdLists.length; i++)
            fwdLists[i] = new HashMap<>();
        for (int i = 0; i < /*fwdLists.length-1*/3; i++) {
            for (Map.Entry<Long, Integer> kv: fwdLists[i].entrySet()) {
                exploreFwd(kv.getKey(), kv.getValue(), fwdLists[i+1]);
            }
        }
    }

    public static void exploreFwd(long removed, int cost, HashMap<Long, Integer> addTo) {
        int left = 0, right = (int) removed&0x1F, j=0;
        if (right == 0)
            right = len-1;
        for (int mid = 1; mid < len - 1; mid++) {
            if (mid == right) {
                left = mid;
                j++;
                right = (int) removed>>(5*j)&0x1F;
                if (right == 0)
                    right = len-1;
            } else {
                System.out.print(left+" "+mid+" "+right+" ==> ");
                long newRemoved = (removed & ((1 << 5*j) -1))
                        | (mid << 5*j)
                        | ((removed << 5) & (-1 << 5*j+5));
                System.out.println(Long.toBinaryString(newRemoved));
                JCalcGains.costs((int) newRemoved & (0b111111111111111 << (5*j)), 42);
            }
        }
    }

    public static void main(String[] args) {
        JCalcGains.calcGains(new Input(20, 321).a);
        len = JCalcGains.a.length;
        System.out.println(Arrays.toString(new Input(20, 321).a));
        System.out.println(Arrays.toString(JCalcGains.maxByLen));
        System.out.println(Arrays.toString(JCalcGains.maxByMid));

//        exploreFwd(5);
//        exploreFwd(3+32*7+(9<<10));



    }
}
