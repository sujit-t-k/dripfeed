package org.ajikhoji.dripfeed.processor;

import org.ajikhoji.dripfeed.model.ProductModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class ProductTableInsertBatchProcessor implements BatchProcessor<ProductModel> {

    private final List<ProductModel> newProducts;

    public ProductTableInsertBatchProcessor(final List<ProductModel> allProducts) {
        newProducts = allProducts;
    }

    @Override
    public void execute(Connection connection) throws SQLException {
        String sql = """
            INSERT INTO Products
            (product_id, product_name, product_category, updated_timestamp)
            VALUES (?, ?, ?, ?)
            """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            for (ProductModel product : newProducts) {
                ps.setInt(1, product.getProductId());
                ps.setString(2, product.getProductName());
                ps.setString(3, product.getProductCategory());
                ps.setTimestamp(4, Timestamp.valueOf(product.getUpdatedTimestamp()));

                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

    @Override
    public int getRecordsCount() {
        return newProducts.size();
    }

}
