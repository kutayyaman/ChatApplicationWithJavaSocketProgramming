package Repository;

import Entity.Chat;
import Entity.User;

import java.util.List;

public interface ChatRepository {
    List<Chat> getAllByAccountIdWithMessages(Integer accountId);

    Chat createAChat(List<User> users, User creatorUser, String chatName);
}
