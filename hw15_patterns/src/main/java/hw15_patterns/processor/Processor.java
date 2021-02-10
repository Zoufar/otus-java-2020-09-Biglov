package hw15_patterns.processor;

import hw15_patterns.model.Message;

public interface Processor {

    Message process (Message message);

// done    //todo: 2. Сделать процессор, который поменяет местами значения field11 и field12
}
