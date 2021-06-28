package protobuf.model;

public class Numbers {

    private long firstNumber;
    private long secondNumber;

    public Numbers (long firstNumber, long secondNumber) {
        this.firstNumber = firstNumber;
        this.secondNumber = secondNumber;
    }

    public long getFirstNumber() {
        return firstNumber;
    }

    public void setFirstNumber(long number) {
        this.firstNumber = number;
    }

    public long getSecondNumber() {
        return secondNumber;
    }

    public void setSecondNumber(long number) {
        this.secondNumber = number;
    }

}
