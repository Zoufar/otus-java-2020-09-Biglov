package hw23webserver.server;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import hw23webserver.helpers.FileSystemHelper;
import hw23webserver.services.TemplateProcessor;
import hw23webserver.servlet.UsersApiServlet;
import hw23webserver.servlet.UsersServlet;
import hw23webserver.services.UserAuthService;
import hw23webserver.servlet.AuthorizationFilter;
import hw23webserver.servlet.LoginServlet;
import hw23webserver.servlet.AdminServlet;
import hw23webserver.servlet.SaveUserServlet;
import hw23webserver.core.service.DBServiceUser;


import java.util.Arrays;

public class UsersWebServerWithFilterBasedSecurity implements UsersWebServer {
    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";

    private final DBServiceUser dbServiceUser;
    private final Gson gson;
    protected final TemplateProcessor templateProcessor;
    private final Server server;

    private final UserAuthService authService;



    public UsersWebServerWithFilterBasedSecurity(int port,
                                                 UserAuthService authService,
                                                 DBServiceUser dbServiceUser,
                                                 Gson gson,
                                                 TemplateProcessor templateProcessor) {

        this.dbServiceUser = dbServiceUser;
        this.gson = gson;
        this.templateProcessor = templateProcessor;
        server = new Server(port);
        this.authService = authService;
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }


    private Server initContext() {

        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(applySecurity(servletContextHandler, "/users", "/api/user/*", "/admin"));


        server.setHandler(handlers);
        return server;
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths).forEachOrdered(path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new UsersServlet(templateProcessor, dbServiceUser)), "/users");
        servletContextHandler.addServlet(new ServletHolder(new UsersApiServlet(dbServiceUser, gson)), "/api/user/*");
        servletContextHandler.addServlet(new ServletHolder(new AdminServlet(templateProcessor, dbServiceUser)), "/admin");
        servletContextHandler.addServlet(new ServletHolder(new SaveUserServlet(templateProcessor, dbServiceUser)), "/save");

        return servletContextHandler;
    }




}
