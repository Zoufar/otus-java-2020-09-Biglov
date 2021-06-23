package springbootjdbc.front.handlers;


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

public class SaveClientBDResponseHandler implements RequestHandler<ClientBD> {
    private static final Logger logger = LoggerFactory.getLogger(SaveClientBDResponseHandler.class);

    private final CallbackRegistry callbackRegistry;

    public SaveClientBDResponseHandler(CallbackRegistry callbackRegistry) {
        this.callbackRegistry = callbackRegistry;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("new message with saved:{}", msg);
        try {
            MessageCallback<? extends ResultDataType> callback = callbackRegistry.getAndRemove(msg.getCallbackId());
            if (callback != null) {
                callback.accept(MessageHelper.getPayload(msg));
            } else {
                logger.error("callback for Id:{} not found", msg.getCallbackId());
            }
        } catch (Exception ex) {
            logger.error("msg:{}", msg, ex);
        }
        return Optional.empty();
    }
}


