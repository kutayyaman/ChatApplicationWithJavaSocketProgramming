package Repository;

import Entity.GlobalChatMessage;

import java.util.List;

public interface GlobalChatMessageRepository {
    GlobalChatMessage add(GlobalChatMessage globalChatMessage);

    List<GlobalChatMessage> getAll();
}
