package Entity;

import java.util.List;

public class Chat {
    private Integer id;
    private String chatName;
    private List<Message> messages;

    public Chat() {
    }

    public Chat(String chatName) {
        this.chatName = chatName;
    }

    public Chat(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    @Override
    public String toString() {
        return String.format("%d-%s", this.id, chatName);
    }
}
