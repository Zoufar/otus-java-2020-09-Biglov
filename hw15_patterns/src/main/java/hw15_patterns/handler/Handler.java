package hw15_patterns.handler;

import hw15_patterns.model.Message;
import hw15_patterns.listener.Listener;

public interface Handler {
    Message handle(Message msg);

    void addListener(Listener listener);
    void removeListener(Listener listener);
}
