package springbootjdbc.front;


import springbootjdbc.crm.model.ClientBD;
import messagesystem.client.MessageCallback;
import messagesystem.client.MsClient;
import messagesystem.message.Message;
import messagesystem.message.MessageType;



public class FrontendServiceBDImpl implements FrontendServiceBD {

    private final MsClient msClient;
    private final String databaseServiceClientName;
//    private final MessageType messageType;


    public FrontendServiceBDImpl (MsClient msClient, String databaseServiceClientName) {
        this.msClient = msClient;
        this.databaseServiceClientName = databaseServiceClientName;
//        this.messageType = messageType;
    }


    @Override
    public void saveClientBD (ClientBD clientBD, MessageCallback<ClientBD> dataConsumer) {
        Message outMsg = msClient.produceMessage(databaseServiceClientName, clientBD,
                MessageType.CLIENTBD_SAVE, dataConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public void getClientBDList (MessageCallback<ClientBD> dataConsumer){
        Message outMsg = msClient.produceMessage(databaseServiceClientName, new ClientBD(0L, null,null,null),
                MessageType.CLIENTBD_GET_LIST, dataConsumer);
        msClient.sendMessage(outMsg);
    }

}
