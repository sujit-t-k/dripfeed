package org.ajikhoji.dripfeed.processor;

import org.ajikhoji.dripfeed.model.CustomerModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class CustomerTableUpdateBatchProcessor implements BatchProcessor<CustomerModel> {

    private final List<CustomerModel> updatedCustomerRecords;

    public CustomerTableUpdateBatchProcessor(final List<CustomerModel> updatedCustomers) {
        updatedCustomerRecords = updatedCustomers;
    }

    @Override
    public void execute(Connection connection) throws SQLException {
        String sql = """
            UPDATE Users
            SET address = ?,
                address_pin = ?,
                updated_timestamp = ?
            WHERE user_id = ?
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            for (CustomerModel customer : this.updatedCustomerRecords) {
                ps.setString(1, customer.getAddress());
                ps.setInt(2, customer.getAddressPin());
                ps.setTimestamp(3, Timestamp.valueOf(customer.getUpdatedTimestamp()));
                ps.setInt(4, customer.getUserId());

                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

    @Override
    public int getRecordsCount() {
        return this.updatedCustomerRecords.size();
    }

}
