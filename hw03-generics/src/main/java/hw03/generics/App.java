package hw03.generics;

import java.util.*;
import java.util.Comparator;

public class App {
    public static void main(String[] args) {
        for(int k=0;k<4;) {
            String s = "\n**********************\n Enter No. of the test to perform:\n";
            s += "1 - addAll test \n";
            s += "2 - copy test \n";
            s += "3 - sort test\n";
            s += "4 - exit";
            k = chooseNumber(s, 4);
            switch (k) {
                case 1:
                    System.out.println("check addAll");
                    checkAddAll();
                    break;
                case 2:
                    System.out.println("check copy.\nnote 2*listIterators starting for array size>=10");
                    checkCopy();
                    break;
                case 3:
                    System.out.println("check sort");
                    checkSort();
                    break;
                case 4:
                    System.out.println("Thank you.  Have a nice day!");
            }
        }
    }


    private static int chooseNumber(String str, int nMax) {
        Scanner sc1 = new Scanner(System.in);
        int number=nMax;
        System.out.println(str);
        do {
            if(sc1.hasNextInt()){
                number = sc1.nextInt();
                if (number>0){
                    System.out.println("Ok, number " + Math.min(number, nMax)+" chosen");
                    return Math.min(number, nMax);
                }
                System.out.println(number+" is not a valid int, should be 1 - "+nMax+". Please retry");
            }
            String string = sc1.next();
            number=-1;
            System.out.println(string+" is not an int, please retry");} while(number<=0);
        return number;
    }

    public static void checkAddAll(){
        List<Cat> cats = new DIYArrayList<>();
        List<Animal> anims = new DIYArrayList<>();
        anims.add(new Animal("Elephant"));
        int length = chooseNumber("Enter array size",100);
        Cat[] catArr=new Cat[length];
        for (int i = 0; i < length; i++) {
            String name="cat"+(i+1);
            catArr[i]=new Cat(name);
        }
        Collections.addAll(anims, new Cat("Sharik"), new Cat("Barsik"));
        Collections.addAll(anims, catArr);

        System.out.println("\n printing anims with added list and array of cats:");
        String text= " * ";
        for (int i = 0; i < anims.size(); i++) {
            text += anims.get(i)+" * ";
        }
        System.out.println(text);
    }

    public static void checkCopy(){
        List<Cat> cats = new DIYArrayList<>();
        List<Animal> anims = new DIYArrayList<>();
        int size = chooseNumber("Enter array size",100);

        for (int i = 0; i < size; i++) {
            anims.add(new Animal("Elephant"));
            String name="cat"+(i+1);
            cats.add(new Cat(name));
        }

        System.out.println("\n printing anims: \n");
        String text= " * ";
        for (int i = 0; i < anims.size(); i++) {
            text += anims.get(i)+" * ";
        }
        System.out.println(text);

        System.out.println("\n copying cats to anims:");
        Collections.copy(anims, cats);


        text = " \n printing cats copied to anims: \n \n * ";
        for (int i = 0; i < cats.size(); i++) {
            text += cats.get(i)+" * ";
        }
        System.out.println(text);
    }

    public static void checkSort(){
        Comparator<Cat> comparCat = (c1, c2) -> c1.getTone().compareTo(c2.getTone());
        Comparator<Animal> comparAnims = (a1, a2) -> a1.getName().compareTo(a2.getName());
        List<Cat> cats1 = new DIYArrayList<>();
        List<Cat> cats2 = new DIYArrayList<>();
        List<Animal> anims = new DIYArrayList<>();
        int size = chooseNumber("Enter array size",100);
        Cat cat= new Cat("cat");
        double tone;

        for (int i = 0; i < size; i++) {
            tone=10*Math.random();
            String name="cat"+(i+1);
            String nameR="cat"+(size-i);
//            cat=new Cat("cat"+(i+1), 10*Math.random())
            anims.add(new Cat(name, tone));
            cats1.add(new Cat(nameR, tone));
            cats2.add(new Cat(name, tone));
        }

//        Collections.copy(anims, cats);
//        Collections.sort(anims, comparCat);
        Collections.sort(cats1, comparAnims);
        Collections.sort(cats2, comparCat);

        String text= "\n cats orginal order:                          ";
        text+="cats1 sorted by name (Comparator<Animal>):           ";
        text+="cats2 sorted by tone: \n\n";
        for (int i = 0; i < size; i++) {
            text += anims.get(i)+"  ||   "+cats1.get(i)+"  ||   "+cats2.get(i)+"\n";
        }
        System.out.println(text);
    }


}
