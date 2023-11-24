package com.example.ass2_i200802_i200707;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemModel implements Parcelable {
    String imgUrl;
    String itemName;
    String itemPrice;
    String itemId;
    String userId;
    String itemDate;
    String itemDescription;

    public ItemModel() {

    }

    public ItemModel(String itemName, String itemPrice, String itemDate, String userId, String imgUrl, String desc) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemDate = itemDate;
        this.userId = userId;
        this.imgUrl = imgUrl;
        this.itemDescription = desc;
    }

    protected ItemModel(Parcel in) {
        imgUrl = in.readString();
        itemName = in.readString();
        itemPrice = in.readString();
        itemId = in.readString();
        userId = in.readString();
        itemDate = in.readString();
        itemDescription = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgUrl);
        dest.writeString(itemName);
        dest.writeString(itemPrice);
        dest.writeString(itemId);
        dest.writeString(userId);
        dest.writeString(itemDate);
        dest.writeString(itemDescription);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ItemModel> CREATOR = new Creator<ItemModel>() {
        @Override
        public ItemModel createFromParcel(Parcel in) {
            return new ItemModel(in);
        }

        @Override
        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemDate() {
        return itemDate;
    }

    public void setItemDate(String itemDate) {
        this.itemDate = itemDate;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
