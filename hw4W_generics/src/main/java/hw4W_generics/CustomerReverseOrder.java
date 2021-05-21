package hw4W_generics;


import java.util.LinkedList;
import java.util.NoSuchElementException;

public class CustomerReverseOrder {

    //todo: 2. надо реализовать методы этого класса
    //надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"

    private final LinkedList <Customer> list = new LinkedList<>();

    public void add(Customer customer) {
        list.add(customer);
    }

    public Customer take() throws NoSuchElementException {
        return list.removeLast();
    }
}
