package hw12_ATM;

import java.util.ArrayList;


public class Cashbox {

    private final int[] DENOMINATIONS = {5000, 1000, 500, 100, 50, 10, 5, 1};
    private ArrayList<Cell> cashboxContent = new ArrayList<>();

    public Cashbox(int[] setOfNomsQttys) {
        int s = Math.min(setOfNomsQttys.length, DENOMINATIONS.length);
         for (int i = 0; i < s; i++) {
            cashboxContent.add(new Cell(i,setOfNomsQttys[i]));
        }
        for (int i = s; i < DENOMINATIONS.length; i++) {
            cashboxContent.add(new Cell(i,0));
        }
     }

    public int numberOfCells(){
        return cashboxContent.size();
    }

    public int getCellQuantity(int i) {
        int cellQtty = cashboxContent.get(i).getQuantity();
        return cellQtty;
    }

    public void addToCell(int i, int q) {
        cashboxContent.get(i).addCellQtty(q);
    }

    public int getBalance() {
        int balance = 0;
        for (int i = 0; i < DENOMINATIONS.length; i++) {
            balance += DENOMINATIONS[i] * cashboxContent.get(i).getQuantity();
        }
        return balance;
    }

    public void addMoneySet(int[] setOfNomsQttys) {
        int s = Math.min(setOfNomsQttys.length, DENOMINATIONS.length);
        for (int i = 0; i < s; i++)
            cashboxContent.get(i).addCellQtty(setOfNomsQttys[i]);
    }

    public String withdrawMoney(int sum) {
        if (sum > this.getBalance()) {
             return "Sorry there is not enough cash in the box";
        }
        int sumRest = sum;
        int[] setOfNomsQttys = {0, 0, 0, 0, 0, 0, 0, 0};
        int amount = 0;

        for (int i = 0; i < DENOMINATIONS.length; i++) {
            setOfNomsQttys[i] = (-1)*availQttyToWithdraw(i, sumRest);
            sumRest += setOfNomsQttys[i] * DENOMINATIONS[i];
           }
                    if (sumRest == 0) {
                        addMoneySet(setOfNomsQttys);
                        return "Here you are";
            } else {
          return "Sorry it is impossible to give out the sum with denominations available";
        }
    }

    private int availQttyToWithdraw(int i, int sum) {
        return Math.min(cashboxContent.get(i).getQuantity(), sum/DENOMINATIONS[i]);
    }
}


