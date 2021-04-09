package hw23webserver.services;

import hw23webserver.core.service.DBServiceUser;
import hw23webserver.model.User;

public class InitDbService {
    private final DBServiceUser dbServiceUser;
    public InitDbService(DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    public void initDbData (User user) {
        dbServiceUser.saveUser(user);
    }
}
