package hw08_gc;


import java.util.ArrayList;
import java.util.List;

class Benchmine implements BenchmineMBean {
    private final int loopCounter;
    private final int gotOut;
    private volatile int size = 0;


    public Benchmine(int loopCounter, int gotOut) {
        this.loopCounter = loopCounter;
        this.gotOut = gotOut;
    }

    void run() throws InterruptedException {
        List<String> garbageGenerator = new ArrayList<>();
        for (int idx = 0; idx < loopCounter; idx++) {
            int local = size;
            for (int i = 0; i < local; i++) {
                garbageGenerator.add(new String(new char[0]));
            }
            for (int k=0;k<gotOut;k++){
            garbageGenerator.remove(garbageGenerator.size()-1);
            }
            Thread.sleep(100); //Label_1
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        System.out.println("new size:" + size);
        this.size = size;
    }
}
