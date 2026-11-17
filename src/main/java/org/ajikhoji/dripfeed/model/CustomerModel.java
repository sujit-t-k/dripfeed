package org.ajikhoji.dripfeed.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class CustomerModel {

    private Integer userId;
    private String userName;
    private String email;
    private String phone;
    private String address;
    private Integer addressPin;
    private String hashedPassword;
    private LocalDateTime updatedTimestamp;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAddressPin() {
        return addressPin;
    }

    public void setAddressPin(Integer addressPin) {
        this.addressPin = addressPin;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public LocalDateTime getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(LocalDateTime updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    @Override
    public String toString() {
        return "CustomerModel{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", addressPin=" + addressPin +
                ", hashedPassword='" + hashedPassword + '\'' +
                ", updatedTimestamp=" + updatedTimestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CustomerModel that = (CustomerModel) o;
        return userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId);
    }

}
