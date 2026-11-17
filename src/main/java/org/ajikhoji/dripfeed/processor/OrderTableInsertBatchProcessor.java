package org.ajikhoji.dripfeed.processor;

import org.ajikhoji.dripfeed.model.OrderModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class OrderTableInsertBatchProcessor implements BatchProcessor<OrderModel> {

    private final List<OrderModel> newOrders;

    public OrderTableInsertBatchProcessor(final List<OrderModel> allOrders) {
        newOrders = allOrders;
    }

    @Override
    public void execute(Connection connection) throws SQLException {
        String sql = """
            INSERT INTO Orders
            (order_id, user_id, order_timestamp, updated_timestamp)
            VALUES (?, ?, ?, ?)
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            for (OrderModel order : newOrders) {
                ps.setInt(1, order.getOrderId());
                ps.setInt(2,order.getUserId());
                ps.setTimestamp(3, Timestamp.valueOf(order.getOrderTimestamp()));
                ps.setTimestamp(4, Timestamp.valueOf(order.getUpdatedTimestamp()));

                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

    @Override
    public int getRecordsCount() {
        return newOrders.size();
    }

}
