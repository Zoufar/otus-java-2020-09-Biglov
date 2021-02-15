package hw15_patterns.processor;


import hw15_patterns.model.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

class ProcessorThrowingExcptnTest {

    @Test
    @DisplayName("Testing Exception Throwing")
    void throwExceptionTest() {
        //given
        final Calendar CALENDAR_EVEN_SECONDS = new GregorianCalendar();
        final Calendar CALENDAR_ODD_SECONDS = new GregorianCalendar();
        CALENDAR_EVEN_SECONDS.set(Calendar.SECOND, 2);

        var message = new Message.Builder(1L).field7("field7").build();

        var myCalendar = mock(MyCalendar.class);
        when(myCalendar.getCalendar()).thenReturn(CALENDAR_EVEN_SECONDS);

        var processor = new ProcessorThrowingExcptn(myCalendar);

        //when
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> {processor.process(message);});

    }

    @Test
    @DisplayName("Testing processing w/o Exception Throwing")
    void processMessageTest() {
        //given
        final Calendar CALENDAR_EVEN_SECONDS = new GregorianCalendar();
        final Calendar CALENDAR_ODD_SECONDS = new GregorianCalendar();
        CALENDAR_ODD_SECONDS.set(Calendar.SECOND, 3);

        var message = new Message.Builder(1L).field7("field7").build();

        var myCalendar = mock(MyCalendar.class);
        when(myCalendar.getCalendar()).thenReturn(CALENDAR_ODD_SECONDS);

        var processor = new ProcessorThrowingExcptn(myCalendar);
        var result = new Message.Builder(2L).field1("field1").build();

        //when
        result = processor.process(message);

        //then
        assertThat(result).isEqualTo(message);
    }

}