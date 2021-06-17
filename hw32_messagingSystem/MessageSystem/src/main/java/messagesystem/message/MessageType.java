package messagesystem.message;

import java.util.HashMap;
import java.util.Map;

public enum MessageType {
    USER_DATA("UserData"), CLIENTBD_SAVE ("ClientBDSave"), CLIENTBD_GET_LIST ("ClientBDGetList");

    final String name;



    MessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static MessageType getMessageType (String name) {
        final Map<String, MessageType> messageTypeMap = new HashMap<>();
        MessageType [] messageTypes = MessageType.values();
        for (MessageType m : messageTypes) {

            messageTypeMap.put(m.getName(), m);
//            System.out.println(m + " : {name} :" + m.name() + " : {getNAme} :" + m.getName());
        }
        return messageTypeMap.get(name);

    }


}
