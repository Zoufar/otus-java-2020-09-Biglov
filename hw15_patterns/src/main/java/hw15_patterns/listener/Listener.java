package hw15_patterns.listener;

import hw15_patterns.model.Message;

public interface Listener {

    void onUpdated(Message oldMsg, Message newMsg);

    //todo: 4. Сделать Listener для ведения истории: старое сообщение - новое (подумайте, как сделать, чтобы сообщения не портились)
}
