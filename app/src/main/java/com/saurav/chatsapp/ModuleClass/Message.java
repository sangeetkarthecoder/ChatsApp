package com.saurav.chatsapp.ModuleClass;

public class Message {

    private String msg_id, msg, sender_id;
    private long timestamp;
    private long feeling = -1;

    public Message() {

    }

    public Message(String msg, String sender_id, long timestamp) {
        this.msg = msg;
        this.sender_id = sender_id;
        this.timestamp = timestamp;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getFeeling() {
        return feeling;
    }

    public void setFeeling(int feeling) {
        this.feeling = feeling;
    }
}
