package com.example.ass2_i200802_i200707;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.function.IntFunction;
import java.util.stream.Stream;

public class User implements Parcelable {

    String name, email, contact, displayUrl, coverUrl, password;
    int userId;

    // Parcelable implementation
    protected User(Parcel in) {
        name = in.readString();
        email = in.readString();
        contact = in.readString();
        password = in.readString();
        displayUrl = in.readString();
        coverUrl = in.readString();
        userId = in.readInt();
    }

    public User() {

        this.name = "";
        this.email = "";
        this.contact = "";
        this.displayUrl = "";
        this.coverUrl = "";
    }

    public User(String name, String email, String contact) {
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.displayUrl = "";
        this.coverUrl = "";
    }
    public User(String name, String email, String contact, String displayUrl, String coverUrl) {
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.displayUrl = displayUrl;
        this.coverUrl = coverUrl;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDisplayUrl() {
        return displayUrl;
    }

    public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(contact);
        dest.writeString(displayUrl);
        dest.writeString(password);
        dest.writeString(coverUrl);
        dest.writeInt(userId);
    }
}
