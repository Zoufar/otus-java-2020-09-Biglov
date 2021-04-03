package hw23webserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import hw23webserver.server.UsersWebServer;
import hw23webserver.server.UsersWebServerWithFilterBasedSecurity;
import hw23webserver.services.TemplateProcessor;
import hw23webserver.services.TemplateProcessorImpl;
import hw23webserver.services.UserAuthService;
import hw23webserver.services.UserAuthServiceImpl;
import hw23webserver.DbService;
import hw23webserver.core.service.DBServiceUser;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // Страница администрирования пользователей
    http://localhost:8080/admin

    // REST сервис
    http://localhost:8080/api/user/3
*/
public class WebServerWithFilterBasedSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {

        DbService dbService = new DbService();
        DBServiceUser dbServiceUser = dbService.getDbServiceUser();

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(dbServiceUser);

        UsersWebServer usersWebServer = new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                authService, dbServiceUser, gson, templateProcessor);

        dbService.setDb();

        usersWebServer.start();
        usersWebServer.join();

        }

}
