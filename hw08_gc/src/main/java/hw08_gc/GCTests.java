package hw08_gc;

import com.sun.management.GarbageCollectionNotificationInfo;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.management.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.management.*;
import javax.management.openmbean.CompositeData;

import static hw08_gc.Benchmine.cycle;

/*
java -Xms400m -Xmx400m -XX:+UseParallelGC -verbose:gc -Xlog:gc*:file=./logs/gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./logs/dump -jar hw08_gc-GCTests-0.1.jar

java -Xms400m -Xmx400m -XX:+UseSerialGC -verbose:gc -Xlog:gc*:file=./logs/gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./logs/dump -jar hw08_gc-GCTests-0.1.jar

java -Xms400m -Xmx400m -XX:+UseG1GC -verbose:gc -Xlog:gc*:file=./logs/gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./logs/dump -jar hw08_gc-GCTests-0.1.jar

*/


public class GCTests {

    static Map<String, List<Long>> results = new HashMap<>();
    static Map<String, Map<Long,List<Long>>> resultsSlip = new HashMap<>();
    static long slipIntrv = 25L;
    static long beginTime=0L;
    static boolean switchOn = true;

    public static void main(String... args) throws Exception {

       boolean append = true;
        boolean autoFlush = true;
        PrintStream out = new PrintStream(new FileOutputStream("output.txt", append), autoFlush);
        System.setOut(out);

        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
        switchingMonitoring(switchOn);
        beginTime = System.currentTimeMillis();

        int size = 4 * 1000 * 200;
        int loopCounter = 9000;
        //int loopCounter = 100000;
        int gotOut = 4 * 1000 * 197;


        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("hw08_gc:type=Benchmine");

        Benchmine mbean = new Benchmine(loopCounter, gotOut);
        mbs.registerMBean(mbean, name);
        mbean.setSize(size);
        try {
            mbean.run();
        } catch (OutOfMemoryError|InterruptedException oomeie) {
            switchingMonitoring(!switchOn);
            finalOut();
            System.out.println("Total time:" + (System.currentTimeMillis() - beginTime) / 1000+" s; cycles run: "+cycle);
            return;
        }
    }

    public static void switchingMonitoring(boolean switchOn) {
        List<GarbageCollectorMXBean> gcbeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            System.out.println("GC name:" + gcbean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    String gcName = info.getGcName();
                    String gcAction = info.getGcAction();
                    String gcCause = info.getGcCause();

                    long startTime = info.getGcInfo().getStartTime();
                    long duration = info.getGcInfo().getDuration();
                    long absTime = (System.currentTimeMillis()-beginTime) / 1000;
                    long absTimeDec = absTime/10;

                    System.out.println("start:" + startTime + "*  absTime: "+ absTime + "* Name:" + gcName + ",* action:" + gcAction + ",* gcCause:" + gcCause + " * (" + duration + " ms), * cycles run: "+ cycle);

                    Map<String, MemoryUsage> memoryUsageBeforeGc = info.getGcInfo().getMemoryUsageBeforeGc();
                    showMemUsageLine(100, memoryUsageBeforeGc);

                    if (results.containsKey(gcName)) {
                        List<Long> list = results.get(gcName);
                        list.set(0, list.get(0) + 1L);
                        list.set(1, list.get(1) + duration);
                    } else {
                        results.put(gcName, new ArrayList<Long>(List.of(1L,duration )));
                    }

                    if (resultsSlip.containsKey(gcName)) {
                        fillResultMap(gcName,absTime,absTimeDec,duration,slipIntrv);
                    }
                    else {
                        resultsSlip.put(gcName, new HashMap<Long, List<Long>>() {{
                            put(absTimeDec, new ArrayList<Long>(List.of(0L, 0L, 0L, 0L)));
                        }});
                        fillResultMap(gcName,absTime,absTimeDec,duration,slipIntrv);
                    }

                }
            };
            if(switchOn){
            emitter.addNotificationListener(listener, null, null);
        } else {
                try {
                    emitter.removeNotificationListener(listener,null,null);
                } catch (ListenerNotFoundException e) {
                    System.out.println("listener _listener_ not found");
                }
                }
            }
    }

    public static void fillResultMap(String gcName, long absTime, long absTimeDec, long duration, long slipIntrv){
        Map<Long,List<Long>> mapSlip = resultsSlip.get(gcName);
        for (long key = (absTime- slipIntrv)>=0? ((absTime- slipIntrv)/10+1L):0L;
             key<=absTimeDec; key++){
            if(mapSlip.containsKey(key)) {
                List<Long> list = mapSlip.get(key);
                if(key==absTimeDec){
                    list.set(0, list.get(0) + 1L);
                    list.set(1, list.get(1) + duration);}
                list.set(2, list.get(2) + 1L);
                list.set(3, list.get(3) + duration);}
            else{
                List<Long> list = new ArrayList<Long>(List.of(0L,0L,1L,duration));
                if(key==absTimeDec){
                    list.set(0, list.get(0) + 1L);
                    list.set(1, list.get(1) + duration);}
                mapSlip.put(key,list);
            }
        }
    }

    public static void showMemUsageLine(int lineLength, Map<String, MemoryUsage> memUsage) {
        int lineLeng = lineLength;
        double length = 1.0;
        String str = "Sum of ";
        int isHeap = 0;
        long beforeUsed = 0;
        long beforeMax = 0;
        for (Map.Entry<String, MemoryUsage> entry : memUsage.entrySet()) {
            String memoryPool = entry.getKey();
            if ((isHeap = isHeap(memoryPool)) > 0) {
                str += "+ " + memoryPool;
                beforeUsed += entry.getValue().getUsed();
                beforeMax += entry.getValue().getMax();
            }
        }
        double percent = length * beforeUsed / beforeMax;
        printLineUsage(lineLeng, percent);
//        System.out.println(str+"  used: "+beforeUsed+"  ;  Max: "+beforeMax);
    }

    public static void printLineUsage(int lineLength, double percent) {
        int lineLen = (int) Math.round(percent * lineLength);
        String str1 = "*";
        for (int i = 0; i < lineLen; i++) {
            str1 += "*";
        }
        for (int i = lineLen; i < lineLength; i++) {
            str1 += "-";
        }
        System.out.println(str1);
    }

    public static int isHeap(String keyUsageMap) {
        List<String> mBeanNames = ManagementFactory.getMemoryPoolMXBeans().stream().
                filter((MemoryPoolMXBean mBean) -> mBean.getType() == MemoryType.HEAP).
                map((MemoryPoolMXBean mBean) -> mBean.getName()).collect(Collectors.toList());
        int isHeap = -1;
        if (mBeanNames.size() == 0) {
            return isHeap;
        }
        isHeap = 0;
        for (var mBeanName : mBeanNames) {
            if (mBeanName.equals(keyUsageMap)) {
                isHeap = 1;
            }
        }
        return isHeap;
    }

    public static void finalOut() {
        for (Map.Entry<String, List<Long>> entry : results.entrySet()) {
            System.out.println(entry.getKey() + ": total CollectionCount=" + entry.getValue().get(0)
                    + " total duration=" + entry.getValue().get(1) + " ms");
        }

        for (String gcName : resultsSlip.keySet()){
            System.out.println("GCName = "+ gcName);
            Map<Long,List<Long>> mapSlip = resultsSlip.get(gcName);
            for (long key : mapSlip.keySet()) {
                List<Long> list = mapSlip.get(key);
                String str="timestamp = "+ key*10 + " s ";
                str += "* pure { N = " + list.get(0) + " * duration = " + list.get(1) + " ms } ";
                str += "* slipping { N = " + list.get(2) + "; * duration = " + list.get(3) + " ms }";
                System.out.println(str);
            }
        }
    }
}
