package hw4W_generics;


import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны

    private final TreeMap <Customer, String> treeMap = new TreeMap<>(Comparator.comparingLong(cust -> cust.getScores()));

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> smallest = treeMap.firstEntry();
        if (smallest != null) {
        Customer customerIn = new Customer(smallest.getKey().getId(),
                                       smallest.getKey().getName(),
                                         smallest.getKey().getScores());
        String data = smallest.getValue();
        treeMap.remove(smallest.getKey());
        treeMap.put(customerIn, data);
        }
        return smallest;
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> nextEntry = treeMap.higherEntry(customer);
        if (nextEntry != null) {
        Customer customerIn = new Customer(nextEntry.getKey().getId(),
                nextEntry.getKey().getName(),
                nextEntry.getKey().getScores());
        String data = nextEntry.getValue();

        treeMap.remove(nextEntry.getKey());
        treeMap.put(customerIn, data);
        }
        return nextEntry;
    }

    public void add(Customer customer, String data) {
        treeMap.put(new Customer(customer.getId(), customer.getName(),
                                 customer.getScores()),data);
    }
}
