package hw06_reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Scanner;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.lang.reflect.InvocationTargetException;

public class App {
    public static void main(String[] args) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        Class<?> clazz=null;
        final String package1 = "hw06_reflections.";
        boolean boo1;
// string entry selects behavior:
// - test filename
// - <Enter> for default Sample1
// - EXIT to cancel program execution
        do{
            boo1=false;
            try {
                String testClassName = enterTestClassName("Please enter test class name (default is Sample1) - or EXIT to leave");
                if(testClassName.contains("EXIT")){
                    System.out.println("Exit done");
                    return;
                }
                clazz = Class.forName(package1 + testClassName);}
            catch(ClassNotFoundException e){
                System.out.println("haven't found that name. Try again!");
                boo1=true; }
        }while(boo1);
        System.out.println(clazz);

        Method[] methodsAll = clazz.getDeclaredMethods();
        System.out.println("\n--- all methods:");
        Arrays.stream(methodsAll).forEach(method -> System.out.println(method.getName()));

        List<Method> methAllSorted = new ArrayList<>();
        methAllSorted = Stream.of("Before", "Test", "After").flatMap(str1 -> Arrays.stream(methodsAll).filter(method -> Arrays.toString(method.getDeclaredAnnotations()).contains(str1))).collect(Collectors.toList());

        System.out.println("\n--- annotated  methods sorted:");
        methAllSorted.stream().forEach(meth -> System.out.println(meth.getName() + "   " + Arrays.toString(meth.getDeclaredAnnotations())));

        Double arg = 0.5;
        double fielD = 1.0;

        Constructor constr = clazz.getConstructor(double.class);
        Object obj = constr.newInstance(1.0);
        System.out.println("\n--- constructor:"+constr.toString()+"\n"+obj.toString());
        Field f = obj.getClass().getDeclaredField("field");
        f.setAccessible(true);
        System.out.println("value:" + f.getDouble(obj));

// int entry n selects behavior:
// - negative cancels program execution
// - zero switches to next annotated method
// - positive calls the method n times with random arg 0<arg<1
//  numbers of successful calls and exceptions thrown are counted
        for(Method meth:methAllSorted) {
            boolean boo=true;
            do {
                int numberOfTries = selectNumberOfTries("\nmethod   " + meth.getName() + " calling. \nPlease enter desired number of tries - or zero to switch to the next method - or negative to leave:");

                if (numberOfTries == 0) {
                    System.out.println("\nswitching to next method");
                    boo=false;
                    continue;
                }
                else if(numberOfTries<0){
                    System.out.println("\nThank you. Have a nice day!");
                    return;}

                int nOK = 0, nExcptn = 0;
                for (int i = 0; i < numberOfTries; i++) {
                    arg = Math.random();
                    try {
                        callMethod(obj, meth.getName(), arg);
                        nOK += 1;
                        //   System.out.println("method   " + meth.getName() + " called");
                    } catch (RuntimeException e) {
                        nExcptn += 1;
                        //   System.out.println("method   " + meth.getName() + " called with exception");
                    }
                }
                System.out.println("number of OK calls=" + nOK + "; number of Exception calls= " + nExcptn);
            }while(boo);
        }
    }


    private static String enterTestClassName(String str) {
        Scanner sc1 = new Scanner(System.in);
        String strIn;
        System.out.println(str);
        strIn = sc1.nextLine();
        if(strIn.isEmpty()){strIn="Sample1";}
        System.out.println("you've entered test Class Name: " + strIn);
        return strIn;
    }

    private static int selectNumberOfTries (String str) {
        Scanner sc1 = new Scanner(System.in);
        System.out.println(str);
        String str2;
        int number=0;
        boolean boo2;
        do {boo2 = false;
            if (sc1.hasNextInt()) {
                number = sc1.nextInt();
                System.out.println("Ok, number " + number + " selected");
                str2 = sc1.nextLine();
                return number;
            }
            else {
                str2 = sc1.nextLine();
                boo2 = true;
                System.out.println(str2 + " is not an int, please retry");
            }
        } while (boo2);
        return number;
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
