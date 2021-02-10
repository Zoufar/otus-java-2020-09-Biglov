package hw15_patterns.listener;

import hw15_patterns.model.Message;

import java.util.*;

public class ListenerArchivator implements Listener {
    private final Map<Calendar, List<Message>> msgArchive = new HashMap<>();

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        List <Message> msgEntry = new ArrayList<>(List.of(oldMsg.copy(), newMsg.copy()));
        Calendar key = new GregorianCalendar();
        msgArchive.put(key,msgEntry);
        System.out.println(key.getTime());
        msgArchive.get(key).stream().map(msg -> msg.toString()).
                forEach(str ->System.out.println("\n from archive: "+str));
    }
}
