package hw06_reflections;

import java.util.Scanner;

public class App {
    public static void main(String[] args) throws ReflectiveOperationException {
        Class<?> clazz = null;
        Class<?> clazzToTest = null;
        final String package1 = "hw06_reflections.";
        boolean boo;
// string entry selects behavior:
// - test filename
// - <Enter> for default Sample1
// - EXIT to cancel program execution
        do {
            boo = false;
            try {
                String testClassName = enterTestClassName("Please enter class to test name (default is Sample1) - or EXIT to leave");
                if (testClassName.contains("EXIT")) {
                    System.out.println("Exit done");
                    return;
                }
                clazzToTest = Class.forName(package1 + testClassName);
                clazz = Class.forName(package1 + testClassName + "Test");
            } catch (ClassNotFoundException e) {
                System.out.println("haven't found that name. Try again!");
                boo = true;
            }
        } while (boo);

        var testFrame = new TestFrame(clazz);
        testFrame.runTests();

    }


    private static String enterTestClassName(String str) {
        Scanner sc1 = new Scanner(System.in);
        String strIn;
        System.out.println(str);
        strIn = sc1.nextLine();
        if (strIn.isEmpty()) {
            strIn = "Sample1";
        }
        System.out.println("you've entered test Class Name: " + strIn);
        return strIn;
    }

 /*   private static int selectNumberOfTries(String str) {
        Scanner sc1 = new Scanner(System.in);
        System.out.println(str);
        String str2;
        int number = 0;
        boolean boo2;
        do {
            boo2 = false;
            if (sc1.hasNextInt()) {
                number = sc1.nextInt();
                System.out.println("Ok, number " + number + " selected");
                str2 = sc1.nextLine();
                return number;
            } else {
                str2 = sc1.nextLine();
                boo2 = true;
                System.out.println(str2 + " is not an int, please retry");
            }
        } while (boo2);
        return number;}
*/
}
