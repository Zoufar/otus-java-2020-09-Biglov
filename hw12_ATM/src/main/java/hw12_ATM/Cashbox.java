package hw12_ATM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Cashbox {
    private final List <Integer> DENOMINATIONS =new ArrayList<>();
    private final List <Integer> SET_OF_ZERO_QTTIES=new ArrayList<>();
    private List<Cell> cashboxContent = new ArrayList<>();
    private final int DENOM_NUMBER;


    private Cashbox(List<Integer> denoms) {
        this.DENOM_NUMBER = denoms.size();
        for (int i = 0; i < denoms.size(); i++) {
            this.DENOMINATIONS.add(denoms.get(i));
            this.SET_OF_ZERO_QTTIES.add(0);
            cashboxContent.add(new Cell(denoms.get(i), 0));
        }
    }
    public static Cashbox initCashbox(List<Integer> denoms){
        System.out.println("\nnew cashbox initiated with denominations:"+denoms);
        return new Cashbox(denoms);
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
        for (int i = 0; i < DENOM_NUMBER; i++) {
            balance += DENOMINATIONS.get(i) * cashboxContent.get(i).getQuantity();
        }
        return balance;
    }

    public void addMoneySet(List<Integer> setOfNomsQttys) {
        int setOfNomsLength = Math.min(setOfNomsQttys.size(), DENOM_NUMBER);
        for (int i = 0; i < setOfNomsLength; i++)
            this.addToCell(i,setOfNomsQttys.get(i));
    }

    public List<Integer>  withdrawMoney(int sum) {
        List<Integer> setOfNomsQttys = new ArrayList<>(SET_OF_ZERO_QTTIES);
        if (sum > this.getBalance()) {
            System.out.println("\nSorry there is not enough cash in the box");
            return setOfNomsQttys;
        }
        int sumRest = sum;

        for (int i = 0; i < DENOM_NUMBER; i++) {
            setOfNomsQttys.set(i, (-1)*availableQttyToWithdraw(i, sumRest));
            sumRest += setOfNomsQttys.get(i) * DENOMINATIONS.get(i);
        }
        if (sumRest == 0) {
            addMoneySet(setOfNomsQttys);
            System.out.println( "\nHere you are");
            return setOfNomsQttys;
        } else {
            System.out.println( "\nSorry it is impossible to give out the sum with denominations available");
            Collections.copy(setOfNomsQttys,SET_OF_ZERO_QTTIES);
            return setOfNomsQttys;
        }
    }

    private int availableQttyToWithdraw(int i, int sumRest) {
        return Math.min(cashboxContent.get(i).getQuantity(), sumRest/ DENOMINATIONS.get(i));
    }
}

