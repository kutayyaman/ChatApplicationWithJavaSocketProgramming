package Repository;

import Entity.Chat;
import Entity.User;

import java.util.List;

public interface ChatRepository {
    List<Chat> getAllByAccountIdWithMessages(Long accountId);

    Chat createAChat(List<User> users, User creatorUser);
}
