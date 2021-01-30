package hw10_instrumentation;

public class App {
    public static void main(String[] args) {
        MyLoggingInterface myLogging = Ioc.createMyLoggingTest();
        myLogging.calculation(6);
        myLogging.calculation(6,8);
        myLogging.calculation(6,8,"helloWorld");
    }
}
