package springbootjdbc.front.handlers;

import messagesystem.message.MessageBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springbootjdbc.crm.model.ClientBD;
import messagesystem.RequestHandler;
import messagesystem.client.CallbackRegistry;
import messagesystem.client.MessageCallback;
import messagesystem.client.ResultDataType;
import messagesystem.message.Message;
import messagesystem.message.MessageHelper;

import java.util.Optional;

public class GetClientBDListResponseHandler implements RequestHandler<ClientBD> {
    private static final Logger logger = LoggerFactory.getLogger(GetClientBDListResponseHandler.class);

    private final CallbackRegistry callbackRegistry;

    public GetClientBDListResponseHandler (CallbackRegistry callbackRegistry) {
        this.callbackRegistry = callbackRegistry;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("new message for list entry received:{}", msg);
        ClientBD clientReceived = null;
        try {
            MessageCallback<? extends ResultDataType> callback = callbackRegistry.getAndRemove(msg.getCallbackId());
            clientReceived = (ClientBD) MessageHelper.getPayload(msg);
            System.out.println("клиент полученный" + clientReceived);
            if (callback != null) {
                callback.accept(MessageHelper.getPayload(msg));
                if (clientReceived.getId() <= 0 ) { return Optional.empty();}
                callbackRegistry.put(msg.getCallbackId(), callback);
                return Optional.of(MessageBuilder.buildReplyMessage(msg, clientReceived));
            } else {
                logger.error("callback for Id:{} not found", msg.getCallbackId());
            }
        } catch (Exception ex) {
            logger.error("msg:{}", msg, ex);
        }
        return Optional.empty();
    }

}
