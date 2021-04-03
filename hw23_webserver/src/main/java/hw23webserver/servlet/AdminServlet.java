package hw23webserver.servlet;

import hw23webserver.core.service.DBServiceUser;
import hw23webserver.model.User;
import hw23webserver.services.TemplateProcessor;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;


public class AdminServlet extends HttpServlet {

    private static final String ADMIN_PAGE_TEMPLATE = "admin.html";
    private static final User USER_SUB = new User(1000L,"aaa", "bbb", "CCC");

    private final DBServiceUser dbServiceUser;
    private final TemplateProcessor templateProcessor;

    public AdminServlet (TemplateProcessor templateProcessor, DBServiceUser dbServiceUser) {
        this.templateProcessor = templateProcessor;
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {

        provideResponseUserList ( response, templateProcessor, dbServiceUser);
    }

    public static void provideResponseUserList ( HttpServletResponse response,
                                                 TemplateProcessor templateProcessor,
                                                 DBServiceUser dbServiceUser)
                                            throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();

        List <List> users = new ArrayList<>();
        dbServiceUser.getAllUsers().stream().map(usOpt -> usOpt.orElse(USER_SUB))
                .map(usr -> Arrays.asList(usr.getId(), usr.getName(),usr.getLogin(), usr.getPassword()))
                .forEachOrdered(list -> users.add(list));
        paramsMap.put("users", users);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(ADMIN_PAGE_TEMPLATE, paramsMap));
    }
}
