package Repository;

import Entity.User;

import java.util.List;

public interface UserRepository {
    User add(User user);

    User getByUserName(String userName);

    Boolean isTheUserExist(String userName, String password);

    List<User> getAll();

    List<User> getByChatId(Integer chatId);
}
