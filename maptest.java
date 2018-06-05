import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.Process;
import java.lang.ProcessBuilder;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class maptest {
    public static void main(String[] args) throws InterruptedException {
        // Test parallel computation of same or different key

        Map<String, String> map = new ConcurrentHashMap<>();
        String tenant = "ee";
        String test = tenant + new Date().getHours();
        Thread t1 = new Thread(() -> System.out.println(map.computeIfAbsent(test, k -> sleepBaby(10000, test))));
        String test1 = tenant + new Date().getHours();
        Thread t2 = new Thread(() -> System.out.println(map.computeIfAbsent(test1, k -> sleepBaby(5000, test1))));

        // Basic Map test
        // list.add(new Integer(5)); System.out.println(list.get(0)); list =
        // map.computeIfAbsent(test,k -> new ArrayList());
        // System.out.println(map.size()); System.out.println(list.get(0));

        /*
         * Test for running a command line utility try { Process process = new
         * ProcessBuilder("ls", "",".").start(); InputStream is =
         * process.getInputStream(); InputStreamReader isr = new InputStreamReader(is);
         * BufferedReader br = new BufferedReader(isr); String line;
         * 
         * // System.out.printf("Output of running %s is:", Arrays.toString(args));
         * 
         * while ((line = br.readLine()) != null) { System.out.println(line); } } catch
         * (Exception e) { }
         */

        // Synchronizing on same/different string key for adding value
        /*
         * Map<String, String> map= new ConcurrentHashMap<>(); String tenant = "ee";
         * 
         * String test = tenant + new Date().getDay(); Thread t1 = new Thread( () ->
         * add(map, test, 10000));
         * 
         * String test1 = "ee" + new Date().getDay(); Thread t2 = new Thread(() ->
         * add(map,test1,1));
         */

        // Test parallel streams
        /*
         * ForkJoinPool customThreadPool = new ForkJoinPool(10); Thread t1 = new
         * Thread(() -> parallel("t1", customThreadPool)); Thread t2 = new Thread(() ->
         * parallel("t2", customThreadPool));
         */
        t1.start();
        System.out.println("T1 starting..");
        t2.start();
        System.out.println("T2 starting..");
        t1.join();
        t2.join();
    }

    static void parallel(String name, ForkJoinPool customThreadPool) {
        long firstNum = 1;
        long lastNum = 100;

        List<Long> aList = LongStream.rangeClosed(firstNum, lastNum).boxed().collect(Collectors.toList());
        try {
            long actualTotal = customThreadPool.submit(() -> aList.parallelStream().reduce((x, y) -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
                System.out.println(name + x + y);
                return x + y;
            })).get().get();
            System.out.println("Result is :" + ((lastNum + firstNum) * lastNum / 2 == actualTotal));
        } catch (Exception e) {

        }
    }

    static void add(Map<String, String> map, String test, int s) {
        String value = map.get(test);
        if (value != null) {
            System.out.println("Key already found with value = " + value);
            return;
        }
        synchronized (test) {
            System.out.println("Entering synchronized block..");
            try {
                Thread.sleep(s);
            } catch (InterruptedException e) {

            }
            map.putIfAbsent(test, test);

        }
    }

    static String sleepBaby(int i, String s) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {

        }
        System.out.println("Computing....");
        return s;
    }
}