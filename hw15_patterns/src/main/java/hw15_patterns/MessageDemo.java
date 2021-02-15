package hw15_patterns;

import hw15_patterns.handler.ComplexProcessor;
import hw15_patterns.listener.*;
import hw15_patterns.model.Message;
import hw15_patterns.model.ObjectForMessage;
import hw15_patterns.processor.*;

import java.util.ArrayList;
import java.util.List;

public class MessageDemo {
    public static void main(String[] args) {
        List <String> data = new ArrayList<>(List.of("one","two","three"));
        var obj13 = new ObjectForMessage();
        obj13.setData(data);
        var processors = List.of(
                new ProcessorConcatFields()
                , new LoggerProcessor(new ProcessorUpperField10())
                , new ProcessorExchangeFields()
                , new ProcessorThrowingExcptn(new MyCalendar())
        );

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {System.out.println("exception caught:"+ex);});
        var listenerPrinter = new ListenerPrinter();
        var listenerArchivator = new ListenerArchivator();
        complexProcessor.addListener(listenerPrinter);
        complexProcessor.addListener(listenerArchivator);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .field13(obj13)
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("\nresult:" + result);

        complexProcessor.removeListener(listenerPrinter);
        complexProcessor.removeListener(listenerArchivator);
    }
}
