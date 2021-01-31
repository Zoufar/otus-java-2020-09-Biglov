package hw08_gc;

import com.sun.management.GarbageCollectionNotificationInfo;

import java.lang.management.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.management.MBeanServer;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;

/*
java -Xms250m -Xmx250m -XX:+UseParallelGC -verbose:gc -Xlog:gc*:file=./logs/gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./logs/dump -jar hw08_gc-GCTests-0.1.jar

java -Xms250m -Xmx250m -XX:+UseSerialGC -verbose:gc -Xlog:gc*:file=./logs/gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./logs/dump -jar hw08_gc-GCTests-0.1.jar

java -Xms250m -Xmx250m -XX:+UseG1GC -verbose:gc -Xlog:gc*:file=./logs/gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./logs/dump -jar hw08_gc-GCTests-0.1.jar

*/

public class GCTests {
    public static void main(String... args) throws Exception {
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
        switchOnMonitoring();
        long beginTime = System.currentTimeMillis();

        int size = 4 * 1000 * 200;
        int loopCounter = 9000;
        //int loopCounter = 100000;
        int gotOut = 4 * 1000 * 198;
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("hw8_gc:type=Benchmark");

        Benchmine mbean = new Benchmine(loopCounter,gotOut);
        mbs.registerMBean(mbean, name);
        mbean.setSize(size);
        mbean.run();

        System.out.println("time:" + (System.currentTimeMillis() - beginTime) / 1000);
    }

    private static void switchOnMonitoring() {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
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
                    Map<String, MemoryUsage> memoryUsageBeforeGc = info.getGcInfo().getMemoryUsageBeforeGc();
                    showMemUsageLine(100, memoryUsageBeforeGc);
                    System.out.println("start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)");

                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }

    public static void showMemUsageLine(int lineLength, Map<String, MemoryUsage> memUsage){
        int lineLeng=lineLength;
        double length=1.0;
        String str="Sum of ";
        int isHeap =0;
        long beforeUsed =0;
        long beforeMax = 0;
        for (Map.Entry<String, MemoryUsage> entry : memUsage.entrySet()) {
            String memoryPool = entry.getKey();
            if((isHeap = isHeap(memoryPool))>0) {
                str+="+ "+memoryPool;
                beforeUsed += entry.getValue().getUsed();
                beforeMax += entry.getValue().getMax();
            }
        }
        double percent=length*beforeUsed/beforeMax;
        printLineUsage(lineLeng, percent);
//        System.out.println(str+"  used: "+beforeUsed+"  ;  Max: "+beforeMax);
    }

    public static void printLineUsage(int lineLength, double percent){
        int lineLen = (int) Math.round(percent*lineLength);
        String str1="*";
        for(int i=0;i<lineLen;i++){str1+="*";}
        for (int i=lineLen;i<lineLength;i++){str1+="-";}
        System.out.println(str1);
    }

    public static int isHeap(String keyUsageMap){
        List<String> mBeanNames = ManagementFactory.getMemoryPoolMXBeans().stream().
                filter((MemoryPoolMXBean mBean) -> mBean.getType()==MemoryType.HEAP).
                map((MemoryPoolMXBean mBean) -> mBean.getName()).collect(Collectors.toList());
            int isHeap=-1;
        if (mBeanNames.size()==0){return isHeap;}
            isHeap=0;
        for(var mBeanName :mBeanNames){
            if (mBeanName.equals(keyUsageMap)){
                isHeap=1;
            }
            }
        return isHeap;
    }
}