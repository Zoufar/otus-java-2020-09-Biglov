package hw23webserver.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import hw23webserver.services.TemplateProcessor;

import hw23webserver.model.User;
import hw23webserver.core.service.DBServiceUser;

import java.io.IOException;


public class SaveUserServlet extends HttpServlet {

    private final DBServiceUser dbServiceUser;
    private static final String PARAM_NAME = "name";
    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";

    private final TemplateProcessor templateProcessor;


    public SaveUserServlet (TemplateProcessor templateProcessor, DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String name = request.getParameter(PARAM_NAME);
        String login = request.getParameter(PARAM_LOGIN);
        String password = request.getParameter(PARAM_PASSWORD);

        User userF = dbServiceUser.getUserByLogin(login).orElse(null);
        if ( userF == null ) {
            dbServiceUser.saveUser(new User( name, login, password ));
        } else {
            userF.setName(name);
            userF.setPassword(password);
            dbServiceUser.saveUser(userF);
        }

        response.sendRedirect("/admin");;

    }

}
