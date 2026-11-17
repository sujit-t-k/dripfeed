package org.ajikhoji.dripfeed.generator;

import org.ajikhoji.dripfeed.config.SimulatorConfig;
import org.ajikhoji.dripfeed.exception.GenerationLimitExceeded;
import org.ajikhoji.dripfeed.model.CustomerModel;
import org.ajikhoji.dripfeed.model.OrderItemModel;
import org.ajikhoji.dripfeed.model.OrderModel;
import org.ajikhoji.dripfeed.model.ProductModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataRecordGenerator {

    private Integer currCustomerId = 0;
    private Integer currProductId = 0;
    private Integer currOrderId = 0;
    private final Random r;

    public DataRecordGenerator() {
        r = new Random();
    }

    private String getRandomPhoneNumber() {
        return String.valueOf(r.nextLong(1_000_000_000L, 9_999_999_999L));
    }

    private String getRandomAddress() {
        int doorNo = r.nextInt(1, 300);
        int street = r.nextInt(1, 15);
        String cross = switch (street) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };

        return String.format("No. %d, %d%s cross street", doorNo, street, cross);
    }

    private Integer getRandomPin() {
        return r.nextInt(100_000, 777_777);
    }

    private String getRandomString(int len) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(len);

        for (int j = 0; j < len; j++) {
            sb.append(chars.charAt(r.nextInt(chars.length())));
        }

        return sb.toString();
    }

    public CustomerModel getNewCustomer() {
        if(currCustomerId >= SimulatorConfig.CUSTOMER_COUNT_CAP) {
            throw new GenerationLimitExceeded("Customer");
        }
        ++currCustomerId;

        final CustomerModel newCustomer = new CustomerModel();
        newCustomer.setUserId(currCustomerId);
        newCustomer.setUserName(String.format("Customer-%d", currCustomerId));
        newCustomer.setEmail(String.format("customer-%d@email.com", currCustomerId));
        newCustomer.setPhone(getRandomPhoneNumber());
        newCustomer.setAddress(getRandomAddress());
        newCustomer.setAddressPin(getRandomPin());
        newCustomer.setHashedPassword(getRandomString(64));
        newCustomer.setUpdatedTimestamp(LocalDateTime.now());

        return newCustomer;
    }

    public ProductModel getNewProduct() {
        if(currProductId >= SimulatorConfig.PRODUCT_COUNT_CAP) {
            throw new GenerationLimitExceeded("Product");
        }
        ++currProductId;

        final ProductModel newProduct = new ProductModel();
        newProduct.setProductId(currProductId);
        newProduct.setProductName(String.format("PROD-%d", currCustomerId));
        newProduct.setProductCategory(String.format("CAT-%d", r.nextInt(1, 20)));
        newProduct.setUpdatedTimestamp(LocalDateTime.now());

        return newProduct;
    }

    private LocalDateTime getRandomPastDateTime() {
        int month = r.nextInt(1, 11);
        int day = r.nextInt(1, 29);
        int hour = r.nextInt(0, 24);
        int min = r.nextInt(0, 60);
        int sec = r.nextInt(0, 60);

        return LocalDateTime.of(2026, month, day, hour, min, sec);
    }

    public OrderModel getNewOrder() {
        if(currOrderId >= SimulatorConfig.ORDER_COUNT_CAP) {
            throw new GenerationLimitExceeded("Order");
        }
        ++currOrderId;

        final OrderModel order = new OrderModel();
        order.setOrderId(currOrderId);
        order.setUserId(r.nextInt(1, currCustomerId + 1));
        order.setOrderTimestamp(getRandomPastDateTime());
        order.setUpdatedTimestamp(LocalDateTime.now());

        return order;
    }

    public OrderItemModel getNewOrderItemForOrder(final Integer orderId) {
        final OrderItemModel orderItem = new OrderItemModel();
        orderItem.setOrderId(orderId);
        orderItem.setProductId(r.nextInt(1, currProductId + 1));
        orderItem.setQuantity(r.nextInt(1, 18));
        orderItem.setUpdatedTimestamp(LocalDateTime.now());

        return orderItem;
    }

    private int previouslyAssignedProductId = 0;
    private int getProductId() {
        previouslyAssignedProductId = (previouslyAssignedProductId % currProductId) + 1;
        return previouslyAssignedProductId;
    }


    public List<OrderItemModel> getNewOrderItemsForOrder(final Integer orderId, Integer count) {
        final List<OrderItemModel> allOrderItems = new ArrayList<>();

        while(count-- > 0) {
            final OrderItemModel orderItem = new OrderItemModel();
            orderItem.setOrderId(orderId);
            orderItem.setProductId(getProductId());
            orderItem.setQuantity(r.nextInt(1, 18));
            orderItem.setUpdatedTimestamp(LocalDateTime.now());
            allOrderItems.add(orderItem);
        }

        return allOrderItems;
    }

    public CustomerModel getUpdatedCustomer() {
        final CustomerModel updated = new CustomerModel();
        updated.setUserId(r.nextInt(1, currCustomerId + 1));
        updated.setAddress(getRandomAddress());
        updated.setAddressPin(getRandomPin());
        updated.setUpdatedTimestamp(LocalDateTime.now());
        return updated;
    }

    public int getCustomersCount() {
        return this.currCustomerId;
    }

    public int getProductsCount() {
        return this.currProductId;
    }

}
