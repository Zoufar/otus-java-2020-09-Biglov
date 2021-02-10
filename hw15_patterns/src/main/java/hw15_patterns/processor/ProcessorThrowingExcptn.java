package hw15_patterns.processor;

import hw15_patterns.model.Message;

import java.util.Calendar;

public class ProcessorThrowingExcptn implements Processor {
    private final Calendar calendar;

    public ProcessorThrowingExcptn(MyCalendar myCalendar){
        this.calendar = myCalendar.getCalendar();
    }

    @Override
    public Message process (Message message) throws RuntimeException {
        if ((calendar.get(Calendar.SECOND)%2) == 0){
            throw new RuntimeException("seconds EVEN_exception: " + calendar.get(Calendar.SECOND));
        }
        System.out.println("seconds ODD: " + calendar.get(Calendar.SECOND));
        return message;
    }
}

