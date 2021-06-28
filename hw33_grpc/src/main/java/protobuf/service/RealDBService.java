package protobuf.service;

import protobuf.model.Numbers;

import java.util.List;

public interface RealDBService {
    List<Numbers> findAllNumbers(long firstNumber, long secondNumber );
}
