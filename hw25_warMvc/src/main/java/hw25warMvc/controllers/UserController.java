package hw25warMvc.controllers;

import hw25warMvc.model.User;
import hw25warMvc.core.service.DBServiceUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class UserController {

        private final DBServiceUser dbServiceUser;

        public UserController(DBServiceUser dbServiceUser) {
            this.dbServiceUser = dbServiceUser;
        }

    @GetMapping({"/"})
    public String startingPageView () {
        return "index.html";
    }

        @GetMapping({ "/user/list"})
        public String usersListView(Model model) {
            List<User> users = dbServiceUser.getAllUsers();
            model.addAttribute("users", users);
            return "usersList.html";
        }

        @GetMapping("/user/create")
        public String userCreateView(Model model) {
            model.addAttribute("user", new User());
            return "userCreate.html";
        }

        @PostMapping("/user/save")
        public RedirectView userSave(@ModelAttribute User user) {
            dbServiceUser.saveUser(user);
            return new RedirectView("/user/list", true);
        }

    }

