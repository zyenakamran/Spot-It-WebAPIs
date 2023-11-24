package com.example.ass2_i200802_i200707;

public class RequestModel
{
    String itemImg, itemId, userId, requesterId, itemName;

    public RequestModel(String itemImg, String userId, String requesterId, String itemName) {
        this.itemImg = itemImg;
        this.userId = userId;
        this.requesterId = requesterId;
        this.itemName = itemName;
    }

    public String getItemImg() {
        return itemImg;
    }

    public void setItemImg(String itemImg) {
        this.itemImg = itemImg;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(String requesterId) {
        this.requesterId = requesterId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
