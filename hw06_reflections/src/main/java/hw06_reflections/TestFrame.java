package hw06_reflections;


import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class TestFrame {
    Class<?> clazz;

    TestFrame(Class<?> cl) {
        this.clazz = cl;
    }

    public void runTests() throws ReflectiveOperationException {
        System.out.println("testing class:          " + clazz);

        Method[] methodsAll = clazz.getDeclaredMethods();
        System.out.println("\n--- all methods:");
        Arrays.stream(methodsAll).
                forEach(method -> System.out.println(method.getName()));


        List <Class> cl=Arrays.asList(Before.class, Test.class, After.class);
        List <List<Method>> methodsSort = new ArrayList<>();
        methodsSort = cl.stream().map(cl1->Arrays.stream(methodsAll).
                filter(method -> method.getDeclaredAnnotation(cl1)!=null).
                collect(Collectors.toList())).collect(Collectors.toList());
        System.out.println("\n--- all methods  sorted:");
        methodsSort.stream().forEach(list ->System.out.println(list));

        var constr = clazz.getConstructor();
        int nOK = 0, nExcptn = 0;

        for(var methodTest:methodsSort.get(1)) {
            var obj = constr.newInstance();
            String str="\nmethod   " + methodTest.getName() + " called";

            for(var methodBefore:methodsSort.get(0)){
                var objForBefore  = constr.newInstance();
                callMethod(objForBefore, methodBefore.getName());}

            try {
                callMethod(obj, methodTest.getName());
                nOK += 1;
            }
            catch (RuntimeException e) {
                nExcptn += 1;
                System.out.println("method   " + methodTest.getName() + " called with exception");
            }
            for(var methodAfter:methodsSort.get(2)){
                var objForAfter  = constr.newInstance();
                callMethod(objForAfter, methodAfter.getName());}
        }
        System.out.println("\n\n total number of methods tested = " + (nOK + nExcptn) + "\n number of OK calls = " + nOK + "\n number of Exception calls = " + nExcptn);
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
