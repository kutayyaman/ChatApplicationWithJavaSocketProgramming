package Entity;

public class Message {
    private Integer id;
    private String body;
    private Integer sender_account_id;
    private Integer chat_id;
    private String senderUserName;

    public Message(Integer id, String body, Integer sender_account_id, Integer chat_id, String senderUserName) {
        this.id = id;
        this.body = body;
        this.sender_account_id = sender_account_id;
        this.chat_id = chat_id;
        this.senderUserName = senderUserName;
    }

    public Message() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getSender_account_id() {
        return sender_account_id;
    }

    public void setSender_account_id(Integer sender_account_id) {
        this.sender_account_id = sender_account_id;
    }

    public Integer getChat_id() {
        return chat_id;
    }

    public void setChat_id(Integer chat_id) {
        this.chat_id = chat_id;
    }

    public String getSenderUserName() {
        return senderUserName;
    }

    public void setSenderUserName(String senderUserName) {
        this.senderUserName = senderUserName;
    }
}
