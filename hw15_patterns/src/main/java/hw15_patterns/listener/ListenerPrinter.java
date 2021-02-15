package hw15_patterns.listener;

import hw15_patterns.model.Message;

public class ListenerPrinter implements Listener {

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        var logString = String.format("Listener notification:\noldMsg:%s, newMsg:%s\n", oldMsg, newMsg);
        System.out.println(logString);
    }
}
