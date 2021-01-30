package hw10_instrumentation;

public class MyLoggingTest implements MyLoggingInterface {

    @Log
    @Override
    public void calculation(int param) {
        System.out.println("Method executed: param="+param);
    }


    @Override
//    @Log
    public void calculation(int param1,int param2) {
        System.out.println("Method executed:  param1="+param1+"; param2="+param2);
    }


    @Override
    @Log
    public void calculation(int param1,int param2, String param3) {
        System.out.println("Method executed: param1="+param1+"; param2="+param2+"; param3="+param3);
    }
}
