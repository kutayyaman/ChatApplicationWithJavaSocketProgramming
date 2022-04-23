package Entity;

public class GlobalChatMessage {
    private Integer id;
    private String body;
    private Integer sender_account_id;
    private String senderUserName;

    public GlobalChatMessage( String body, Integer sender_account_id, String senderUserName) {
        this.body = body;
        this.sender_account_id = sender_account_id;
        this.senderUserName = senderUserName;
    }

    public GlobalChatMessage(Integer id, String body, Integer sender_account_id, String senderUserName) {
        this.id = id;
        this.body = body;
        this.sender_account_id = sender_account_id;
        this.senderUserName = senderUserName;
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

    public String getSenderUserName() {
        return senderUserName;
    }

    public void setSenderUserName(String senderUserName) {
        this.senderUserName = senderUserName;
    }
}
