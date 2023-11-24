package com.example.ass2_i200802_i200707;

public class MessageModel {
    private String messageId;
    String msg;
    String displayUrl;
    String imgUrl;
    String timestamp;
    String messageType;
    String userId;

    public MessageModel(){}
    public MessageModel(String msg, String displayUrl, String timestamp, String messageType, String userId) {
        this.msg = msg;
        this.displayUrl = displayUrl;
        this.timestamp = timestamp;
        this.messageType = messageType;
        this.userId = userId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDisplayUrl() {
        return displayUrl;
    }

    public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
