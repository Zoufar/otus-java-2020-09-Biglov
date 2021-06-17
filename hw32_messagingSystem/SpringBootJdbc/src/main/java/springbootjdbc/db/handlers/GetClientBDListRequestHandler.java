package springbootjdbc.db.handlers;

import springbootjdbc.crm.model.ClientBD;
import springbootjdbc.crm.service.DBServiceClient;

import messagesystem.RequestHandler;
import messagesystem.message.Message;
import messagesystem.message.MessageBuilder;
import messagesystem.message.MessageHelper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class GetClientBDListRequestHandler implements RequestHandler<ClientBD> {

    private final DBServiceClient dbServiceClient;
    public final List <ClientBD> clientBDList;

    public GetClientBDListRequestHandler(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
        clientBDList = dbServiceClient.findAll();
    }

    @Override
    public Optional<Message> handle(Message msg) {
        ClientBD clientBD = MessageHelper.getPayload(msg);
        long clientId = clientBD.getId();
       if (clientId == 0) {
           clientBDList.clear();
           clientBDList.addAll(dbServiceClient.findAll());
             }
        if (clientId >= clientBDList.size()){
            ClientBD data = new ClientBD(-1L, null, null, null);
            return Optional.of(MessageBuilder.buildReplyMessage(msg, data));
        }
        ClientBD data = clientBDList.get((int) clientId);
        return Optional.of(MessageBuilder.buildReplyMessage(msg, data));
    }
}
