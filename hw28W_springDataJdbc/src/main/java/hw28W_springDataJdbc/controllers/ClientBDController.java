package hw28W_springDataJdbc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import hw28W_springDataJdbc.crm.model.ClientBD;
import hw28W_springDataJdbc.crm.service.DBServiceClient;

import java.util.List;

@Controller
public class ClientBDController {

    private final DBServiceClient dBServiceClient;

    public ClientBDController (DBServiceClient dBServiceClient) {
        this.dBServiceClient = dBServiceClient;
    }

    @GetMapping({"/"})
    public String startingPageView () {
        return "index.html";
    }

    @GetMapping({ "/client/list"})
    public String clientsListView(Model model) {
        List<ClientBD> clients = dBServiceClient.findAll();
        model.addAttribute("clients", clients);
        return "clientsList.html";
    }

    @GetMapping("/client/create")
    public String clientCreateView(Model model) {
        model.addAttribute("client", new ClientBD());
        return "clientCreate.html";
    }

    @PostMapping("/client/save")
    public RedirectView clientSave(@ModelAttribute ClientBD client) {
        dBServiceClient.saveClient(client);
        return new RedirectView("/client/list", true);
    }

}
