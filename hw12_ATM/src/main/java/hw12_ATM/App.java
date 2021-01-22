package hw12_ATM;

import java.util.*;

public class App {
    public static void main(String[] args) {
        final List<Integer> DENOMINATIONS = enterSetOfDenominations();
        final int SUM_MAX=1000000;

        Cashbox box = Cashbox.initCashbox(DENOMINATIONS);
        addMoneyToBox(box);

        String str= "\ninitial quantity of denominations: ";
        for(int k=0;k<box.numberOfCells();k++){
            str+=DENOMINATIONS.get(k)+" - "+box.getCellQuantity(k)+" pcs; ";}
        System.out.println(str);

        int balance= box.getBalance();
        System.out.println("\nbalance ="+balance);

        int sum;
        List<Integer> setOfNomsWithdrawn;

        do{
            balance= box.getBalance();
            str = "\n**********************\n Enter sum to withdraw (\"\"0\"\" to EXIT):\n";
            sum = chooseNumber(str, SUM_MAX);

            setOfNomsWithdrawn=box.withdrawMoney(sum);

            str= "\nquantities of denominations handed over: ";
            String str1="\n\nremaining quantities in cells: ";
            int sum1=0;
            for(int k=0;k<box.numberOfCells();k++){
                str+=DENOMINATIONS.get(k)+"  -  "+(-1)*setOfNomsWithdrawn.get(k)+" pcs; ";
                str1+=DENOMINATIONS.get(k)+"  -  "+box.getCellQuantity(k)+" pcs; ";
                sum1+=DENOMINATIONS.get(k)*(-1)*setOfNomsWithdrawn.get(k);
            }
            str+="\n\n Sum withdrawn:"+sum1;
            str+="\n Should remain: "+(balance-sum1);
            str+="\n Final balance: "+box.getBalance();
            System.out.println(str+str1);

        }while(sum!=0 && box.getBalance()!=0);
    }


    private static int chooseNumber(String str, int nMax) {
        Scanner sc1 = new Scanner(System.in);
        int number=nMax;
        System.out.println(str);
        do {
            if(sc1.hasNextInt()){
                number = sc1.nextInt();
                if (number>=0){
                    System.out.println("Ok, number " + Math.min(number, nMax)+" chosen");
                    return Math.min(number, nMax);
                }
                System.out.println(number+" is not a valid int, should be 0 - "+nMax+". Please retry");
            }
            String string = sc1.next();
            number=-1;
            System.out.println(string+" is not an int, please retry");} while(number<=0);
        return number;
    }

    private static void addMoneyToBox(Cashbox box) {
        List<Integer> setOfNomsQttys =Arrays.asList(100,100,100, 100, 100, 100, 5, 4);
        box.addMoneySet(setOfNomsQttys);
    }

    private static List<Integer> enterSetOfDenominations(){
        return Arrays.asList(5000, 1000, 500, 100, 50, 10, 5, 1);
    };

}

