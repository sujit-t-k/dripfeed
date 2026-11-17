package org.ajikhoji.dripfeed.processor;

import org.ajikhoji.dripfeed.model.OrderItemModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class OrderItemTableInsertBatchProcessor implements BatchProcessor<OrderItemModel> {

    private final List<OrderItemModel> newOrderItems;

    public OrderItemTableInsertBatchProcessor(final List<OrderItemModel> allOrderItems) {
        newOrderItems = allOrderItems;
    }

    @Override
    public void execute(Connection connection) throws SQLException {
        String sql = """
            INSERT INTO OrderItems
            (order_id, product_id, quantity, updated_timestamp)
            VALUES (?, ?, ?, ?)
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            for (OrderItemModel orderItem : newOrderItems) {
                ps.setInt(1, orderItem.getOrderId());
                ps.setInt(2, orderItem.getProductId());
                ps.setInt(3, orderItem.getQuantity());
                ps.setTimestamp(4, Timestamp.valueOf(orderItem.getUpdatedTimestamp()));

                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

    @Override
    public int getRecordsCount() {
        return newOrderItems.size();
    }

}
