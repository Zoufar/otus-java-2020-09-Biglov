package hw15_patterns.processor;

import hw15_patterns.model.Message;

public class ProcessorExchangeFields implements Processor {

    @Override
    public Message process(Message message) {
        String str = message.getField11();
        return message.toBuilder().field11(message.getField12()).field12(str).build();
    }
}
