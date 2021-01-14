package hw06_reflections;


import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class TestFrame {
    Class<?>clazz;
    TestFrame(Class<?>cl){this.clazz=cl;}

    public void runTests()  throws ReflectiveOperationException {
        System.out.println("testing class:          "+ clazz);

        Method[] methodsAll = clazz.getDeclaredMethods();
        System.out.println("\n--- all methods:");
        Arrays.stream(methodsAll).
                forEach(method -> System.out.println(method.getName()));

        List<Method> methodsBefore = new ArrayList<>();
        List<Method> methodsAfter = new ArrayList<>();
        List<Method> methodsTest = new ArrayList<>();
        methodsBefore = Arrays.stream(methodsAll).
                filter(method -> method.getDeclaredAnnotation(Before.class)!=null).
                collect(Collectors.toList());
        methodsAfter = Arrays.stream(methodsAll).
                filter(method -> method.getDeclaredAnnotation(After.class)!=null).
                collect(Collectors.toList());
        methodsTest = Arrays.stream(methodsAll).
                filter(method -> method.getDeclaredAnnotation(Test.class)!=null).
                collect(Collectors.toList());

        var constr = clazz.getConstructor();
        int nOK = 0, nExcptn = 0;

        for(var methodTest:methodsTest) {
            var obj = constr.newInstance();
            String str="method   " + methodTest.getName() + " called";

            for(var methodBefore:methodsBefore){callMethod(obj, methodBefore.getName());}
            try {
                callMethod(obj, methodTest.getName());
                nOK += 1;
            }
            catch (RuntimeException e) {
                nExcptn += 1;
                System.out.println("method   " + methodTest.getName() + " called with exception");
            }
            for(var methodAfter:methodsAfter){callMethod(obj, methodAfter.getName());}
        }
        System.out.println("\n\n total number of methods tested = "+(nOK+nExcptn)+"\n number of OK calls = " + nOK + "\n number of Exception calls = " + nExcptn);
    }



    public static Object callMethod(Object object, String name, Object... args) {
        try {
            var method = object.getClass().getDeclaredMethod(name, toClasses(args));
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?>[] toClasses(Object[] args) {
        return Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
    }

}
