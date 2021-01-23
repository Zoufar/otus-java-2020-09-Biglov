package hw10_instrumentation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Ioc {

    private Ioc() {
    }

    static MyLoggingInterface createMyLoggingTest() {
        InvocationHandler handler = new DemoInvocationHandler(new MyLoggingTest());
        return (MyLoggingInterface) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{MyLoggingInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final MyLoggingInterface myLoggingTest;
        private Class <?> clazz;
        private Method[] methodsAll;
        private List<Method> methsAnntd;
        private List<String> methsAnntdSimpleNames;
        DemoInvocationHandler(MyLoggingInterface myLoggingTest) {
            this.myLoggingTest = myLoggingTest;
            clazz = myLoggingTest.getClass();
            methodsAll = clazz.getDeclaredMethods();
            methsAnntd=Arrays.stream(methodsAll).
                filter(method -> method.getDeclaredAnnotation(Log.class)!=null).
                    collect(Collectors.toList());
            methsAnntdSimpleNames = methsAnntd.stream().
                map(meth -> meth.toString().substring(meth.toString().lastIndexOf("calculation"))).
                    collect(Collectors.toList());
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            methsAnntdSimpleNames.stream().
                 filter(methSiNm -> methSiNm.equals(method.toString().substring(method.toString().lastIndexOf("calculation")))).
                    forEach(methSiNm -> System.out.println("\nLogging: method invoked via proxy hahdler:"+methSiNm+",  with param(s): "
                            + Arrays.toString(args)));
            return method.invoke(myLoggingTest, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + myLoggingTest +
                    '}';
        }
    }
}

