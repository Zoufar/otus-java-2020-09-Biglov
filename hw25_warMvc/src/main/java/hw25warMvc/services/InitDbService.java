package hw25warMvc.services;

import hw25warMvc.core.service.DBServiceUser;
import hw25warMvc.model.User;

public class InitDbService {
    private final DBServiceUser dbServiceUser;
    public InitDbService(DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    public void initDbData (User user) {
        dbServiceUser.saveUser(user);

    }

    public void initDbUsers(){
        initDbData(new User("user1", "user1", "11111"));
        initDbData(new User("user2", "user2", "22222"));
        initDbData(new User("user3", "user3", "33333"));
    }
}
