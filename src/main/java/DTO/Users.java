package DTO;

import java.io.BufferedWriter;
import java.util.Arrays;
import java.util.Objects;

public class Users {
    private int userId;
    private String username;
    private String password;
    private String fullName;
    private byte[] avatarUrl;
    private String email;
    private String nickName;
    private String hashed_Password;
    private String place;
    public BufferedWriter writer;

    public Users() {
    }

    public Users(String username, BufferedWriter writer) {
        this.username = username;
        this.writer = writer;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public byte[] getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(byte[] avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHashed_Password() {
        return hashed_Password;
    }

    public void setHashed_Password(String hashed_Password) {
        this.hashed_Password = hashed_Password;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return userId == users.userId && Objects.equals(username, users.username) && Objects.equals(password, users.password) && Objects.equals(fullName, users.fullName) && Arrays.equals(avatarUrl, users.avatarUrl) && Objects.equals(email, users.email) && Objects.equals(nickName, users.nickName) && Objects.equals(hashed_Password, users.hashed_Password) && Objects.equals(place, users.place) && Objects.equals(writer, users.writer);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(userId, username, password, fullName, email, nickName, hashed_Password, place, writer);
        result = 31 * result + Arrays.hashCode(avatarUrl);
        return result;
    }
}
