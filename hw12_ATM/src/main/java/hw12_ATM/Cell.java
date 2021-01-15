package hw12_ATM;

public class Cell {
    int denominationNumber;
    int quantity;

    public Cell(int denominationNumber, int quantity) {
        this.denominationNumber = denominationNumber;
        this.quantity = quantity;
    }

    public int getQuantity(){ return this.quantity; }

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
            throw new RuntimeException("addCellQtty Added negative to big");
        }
    }
}
