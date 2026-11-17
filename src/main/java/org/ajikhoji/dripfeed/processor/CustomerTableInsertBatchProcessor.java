package org.ajikhoji.dripfeed.processor;

import org.ajikhoji.dripfeed.model.CustomerModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class CustomerTableInsertBatchProcessor implements BatchProcessor<CustomerModel> {

    private final List<CustomerModel> newCustomers;

    public CustomerTableInsertBatchProcessor(final List<CustomerModel> newCustomers) {
        this.newCustomers = newCustomers;
    }

    @Override
    public void execute(Connection connection) throws SQLException {
        String sql = """
            INSERT INTO Users
            (user_id, user_name, email, phone, address, address_pin, hashed_password, updated_timestamp)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            for (CustomerModel customer : this.newCustomers) {
                ps.setInt(1, customer.getUserId());
                ps.setString(2, customer.getUserName());
                ps.setString(3, customer.getEmail());
                ps.setString(4, customer.getPhone());
                ps.setString(5, customer.getAddress());
                ps.setInt(6, customer.getAddressPin());
                ps.setString(7, customer.getHashedPassword());
                ps.setTimestamp(8, Timestamp.valueOf(customer.getUpdatedTimestamp()));

                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

    @Override
    public int getRecordsCount() {
        return newCustomers.size();
    }

}
