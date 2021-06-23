package protobuf.service;

import protobuf.model.Numbers;

import java.util.ArrayList;
import java.util.List;

public class RealDBServiceImpl implements RealDBService {

    private final List <Numbers> numbersList;

    public RealDBServiceImpl() {
        numbersList = new ArrayList<>();
    }

    @Override
    public List<Numbers> findAllNumbers( long firstNumber, long secondNumber ){
        numbersList.clear();
        long first = firstNumber <= secondNumber? firstNumber : secondNumber;
        long second = firstNumber >= secondNumber? firstNumber : secondNumber;
        for ( long i = first; i <= second; i++){
            numbersList.add(new Numbers(i, second));
        }
        return numbersList;
    }

}
