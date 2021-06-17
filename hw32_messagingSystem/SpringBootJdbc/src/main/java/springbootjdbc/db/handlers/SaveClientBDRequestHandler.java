package springbootjdbc.db.handlers;

import springbootjdbc.crm.model.ClientBD;
import springbootjdbc.crm.service.DBServiceClient;

import messagesystem.RequestHandler;
import messagesystem.message.Message;
import messagesystem.message.MessageBuilder;
import messagesystem.message.MessageHelper;

import java.util.Optional;


public class SaveClientBDRequestHandler implements RequestHandler<ClientBD> {

    private final DBServiceClient dbServiceClient;

    public SaveClientBDRequestHandler (DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;}

    @Override
    public Optional<Message> handle(Message msg) {
        ClientBD clientBD = MessageHelper.getPayload(msg);

        ClientBD data = dbServiceClient.saveClient(clientBD);
        return Optional.of(MessageBuilder.buildReplyMessage(msg, data));
    }

}
