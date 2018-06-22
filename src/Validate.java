import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Validate {
    public static void main(String[] args) {
        validate(10, 321, 2199622, new int[]{8, 4, 3, 2, 2, 1, 1, 1});
        assertTrue(args.length==2, "need two params: <n> <seed>");
        Scanner s = new Scanner(System.in);
        String line;
        int gain = -1;
        int[] steps = null;
        while (s.hasNextLine()) {
            line = s.nextLine();//.trim();
//            System.out.println("in: "+line);
            if (line.matches("bestGain = [0-9]+") && gain == -1) {
                gain = Integer.parseInt(line.substring(11));
            } else if (line.matches("[0-9]+") && gain == -1) {
                assertTrue(false, "\"bestGain = \" is required ");
                //gain = Integer.parseInt(line);
            } else if (line.matches("steps:( [0-9]+)+")) {
                assertTrue(steps == null, "multiple steps outputs");
                assertTrue(gain != -1, "please output first gain, then steps");
                String[] raw = line.substring(7).split(" ");
                steps = new int[raw.length];
                for (int i = 0; i < raw.length; i++) {
                    steps[i] = Integer.parseInt(raw[i]);
                }
            } else {
                assertTrue(false, "unexpected line \""+line+"\"\n\n" +
                        "Output format should match:\nbestGain = [0-9]+\nsteps:( [0-9]+)+\n");
            }
        }
        try {
            validate(Integer.parseInt(args[0]), Integer.parseInt(args[1]), gain, steps);
        } catch (AssertionError err) {
            System.out.println("gain  = "+gain);
            System.out.println("steps = "+java.util.Arrays.toString(steps));
            System.out.println("n     = "+Integer.parseInt(args[0]));
            System.out.println("seed  = "+Integer.parseInt(args[1]));
            throw err;
        }
//        System.out.println("result correct");
    }

    public static void validate(int n, int seed, int gain, int[] steps) {
        assertTrue( n >= 2, "n(="+n+") must be >= 2");
        assertTrue( gain >= 0, "gain(="+gain+") must be non-negative");
        assertTrue( steps.length == n-2, "number of steps is wrong");
        Random rnd = new Random(seed);
        ArrayList<Integer> a = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            a.add(rnd.nextInt(100));
        }
        ArrayList<Integer> originalA = new ArrayList<>(a);
        int g = 0;
        for (int i = 0; i < steps.length; i++) {
            int remove = steps[i];
            assertTrue(remove > 0, "step "+i+" to small ("+remove+")");
            assertTrue(remove < a.size()-1, "step "+i+" to big ("+remove+")");
            g += Prime.largestPrimeFac(a.get(remove-1), a.get(remove), a.get(remove+1));
            a.remove(remove);
        }
        assertTrue( gain == g, "steps and gain do not match (gain="+gain+" != "+g+"= <gain by steps>)");
        a = new ArrayList<>(originalA);
        int[] arr = new int[n];
        for (int i = 0; i < a.size(); i++) {
            arr[i] = a.get(i);
        }
        Prime.init(arr);
//        assertTrue(gain == Prime.gainIt(), "gain not perfect: gain="+gain+" != "+Prime.gainIt()+"= <gold result>");
        assertTrue(gain == Prime.gain(0, n-1), "gain not perfect: gain="+gain+" != "+Prime.gain(0, n-1)+"= <gold result>");
    }

    public static void assertTrue(Boolean expr, String msg) {
        if (!expr) {
            if (msg == null)
                throw new AssertionError();
            else
                throw new AssertionError(msg);
        }
    }
}
