package springbootjdbc.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import springbootjdbc.crm.model.ClientBD;
import springbootjdbc.front.FrontendServiceBD;

import java.util.ArrayList;
import java.util.List;


@Controller
public class ClientBDController {
    private static final Logger logger = LoggerFactory.getLogger(ClientBDController.class);
    private final SimpMessagingTemplate template;
    private final FrontendServiceBD frontendServiceBD;

    public ClientBDController(SimpMessagingTemplate template, FrontendServiceBD frontendServiceBD) {
        this.template = template;
        this.frontendServiceBD = frontendServiceBD;
    }

    @MessageMapping("/client")
    public void saveClientBD(@Payload ClientBD client) {
        logger.info("got data of client to save:{}", client);
        client.nullID();
        frontendServiceBD.saveClientBD(client, data -> {
                template.convertAndSend("/topic/clientSaved", data);
                logger.info("sent saved clientBD data:{}", data);
                });
    }

    @MessageMapping("/getClientBDList")
    public void getClientBDList (String string) {
        logger.info("Received handshake from Ws:{}", string);
        List<ClientBD> clientBDList = new ArrayList<>();
        frontendServiceBD.getClientBDList(
                data -> {
                    if (data.getId() > 0) {
                        clientBDList.add(data);
                    } else {
                        logger.info("got data  for list clientBD:{}", clientBDList);
                        template.convertAndSend("/topic/getClientBDList", clientBDList);
                    }
                });
    }

}
