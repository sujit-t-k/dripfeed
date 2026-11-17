package org.ajikhoji.dripfeed.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class OrderModel {

    private Integer orderId;
    private Integer userId;
    private LocalDateTime orderTimestamp;
    private LocalDateTime updatedTimestamp;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getOrderTimestamp() {
        return orderTimestamp;
    }

    public void setOrderTimestamp(LocalDateTime orderTimestamp) {
        this.orderTimestamp = orderTimestamp;
    }

    public LocalDateTime getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(LocalDateTime updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderModel that = (OrderModel) o;
        return orderId == that.orderId && userId == that.userId && Objects.equals(orderTimestamp, that.orderTimestamp) && Objects.equals(updatedTimestamp, that.updatedTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, userId, orderTimestamp, updatedTimestamp);
    }

}
