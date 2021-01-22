package hw12_ATM;

public class Cell {
    private final int DENOMINATION_NUMBER;
    private int quantity;

    public Cell(int denominationNumber, int quantity) {
        this.DENOMINATION_NUMBER = denominationNumber;
        this.quantity = quantity;
    }

    public int getQuantity(){
        int quant=this.quantity;
        return quant; }

    public void setQuantity(int q) {
        if (q >= 0) {
            quantity = q;
        } else {
            throw new RuntimeException("Quantity shouldn't be negative");
        }
    }

    public void addCellQtty(int q) {
        if (quantity + q >= 0) {
            quantity += q;
        } else {
            throw new RuntimeException("addCellQtty tried to add negative to big");
        }
    }
}