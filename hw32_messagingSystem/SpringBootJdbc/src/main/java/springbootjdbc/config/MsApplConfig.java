package springbootjdbc.config;

import messagesystem.HandlersStore;
import messagesystem.HandlersStoreImpl;
import messagesystem.MessageSystem;
import messagesystem.MessageSystemImpl;
import messagesystem.client.CallbackRegistry;
import messagesystem.client.CallbackRegistryImpl;
import messagesystem.client.MsClient;
import messagesystem.client.MsClientImpl;
import messagesystem.message.MessageType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springbootjdbc.crm.service.DBServiceClient;
import springbootjdbc.db.handlers.GetClientBDListRequestHandler;
import springbootjdbc.db.handlers.SaveClientBDRequestHandler;
import springbootjdbc.front.FrontendServiceBD;
import springbootjdbc.front.FrontendServiceBDImpl;
import springbootjdbc.front.handlers.GetClientBDListResponseHandler;
import springbootjdbc.front.handlers.SaveClientBDResponseHandler;


@Configuration
public class MsApplConfig {

    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    @Bean
    public MessageSystem messageSystem () {return new MessageSystemImpl();}

    @Bean
    public CallbackRegistry callbackRegistry () { return new CallbackRegistryImpl();}

    @Bean
    public MsClient databaseMsClient (MessageSystem  messageSystem, CallbackRegistry callbackRegistry, DBServiceClient dbServiceClient ) {
        HandlersStore requestHandlerDatabaseStore = new HandlersStoreImpl();
        requestHandlerDatabaseStore.addHandler(MessageType.CLIENTBD_SAVE, new SaveClientBDRequestHandler(dbServiceClient));
        requestHandlerDatabaseStore.addHandler(MessageType.CLIENTBD_GET_LIST, new GetClientBDListRequestHandler(dbServiceClient));

        MsClient databaseMsClient = new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME,
                messageSystem, requestHandlerDatabaseStore, callbackRegistry);
        messageSystem.addClient(databaseMsClient);
        return databaseMsClient;
    }

    @Bean
    public MsClient frontendMsClient (MessageSystem  messageSystem, CallbackRegistry callbackRegistry ) {
        HandlersStore requestHandlerFrontendStore = new HandlersStoreImpl();
        requestHandlerFrontendStore.addHandler(MessageType.CLIENTBD_SAVE, new SaveClientBDResponseHandler(callbackRegistry));
        requestHandlerFrontendStore.addHandler(MessageType.CLIENTBD_GET_LIST, new GetClientBDListResponseHandler(callbackRegistry));

        MsClient frontendMsClient = new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME,
                messageSystem, requestHandlerFrontendStore, callbackRegistry);
        messageSystem.addClient(frontendMsClient);

        return frontendMsClient;
    }

    @Bean
    public FrontendServiceBD frontendServiceBD (@Qualifier("frontendMsClient") MsClient msClient) {
        return new FrontendServiceBDImpl(msClient, DATABASE_SERVICE_CLIENT_NAME);
    }

}
