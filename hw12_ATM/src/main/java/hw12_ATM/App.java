package hw12_ATM;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        final int[] setOfNomsQttysInit = {100, 100, 100, 100, 100, 100, 100, 100};
        int sumMax=1000000;
        Cashbox box = new Cashbox(setOfNomsQttysInit);

        int[] setOfNomsQttys = {10, 10, 10, 10, 10, 10, 10, 11};

        box.addMoneySet(setOfNomsQttys);

        for(int k=0;k<box.numberOfCells();k++){
            System.out.println(box.getCellQuantity(k));
        }

        int balance= box.getBalance();
        System.out.println("\nbalance ="+balance);

        String str = "\n**********************\n Enter sum to withdraw:\n";
        int sum = chooseNumber(str, sumMax);

        str = box.withdrawMoney(sum);
        System.out.println(str);
        if(str.contains("Sorry")){return;}
        str="\n Sum withdrawn:"+sum;
        str+="\n Should remain: "+(balance-sum);
        str+="\n Final balance: "+box.getBalance();
        System.out.println(str);
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
}

