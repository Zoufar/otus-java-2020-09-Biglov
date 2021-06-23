package springbootjdbc.front;

import springbootjdbc.crm.model.ClientBD;
import messagesystem.client.MessageCallback;


public interface FrontendServiceBD {

    void saveClientBD (ClientBD clientBD, MessageCallback<ClientBD> dataConsumer);

    void getClientBDList ( MessageCallback<ClientBD> dataConsumer);

}
