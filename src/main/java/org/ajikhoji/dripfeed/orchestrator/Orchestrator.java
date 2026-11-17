package org.ajikhoji.dripfeed.orchestrator;

import org.ajikhoji.dripfeed.config.SimulatorConfig;
import org.ajikhoji.dripfeed.generator.DataRecordGenerator;
import org.ajikhoji.dripfeed.model.CustomerModel;
import org.ajikhoji.dripfeed.model.OrderItemModel;
import org.ajikhoji.dripfeed.model.OrderModel;
import org.ajikhoji.dripfeed.model.ProductModel;
import org.ajikhoji.dripfeed.processor.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Orchestrator {

    private final Connection conn;
    private Random r;
    private final DataRecordGenerator generator;
    private long totalOperationDone = 0;

    public Orchestrator(final Connection connection) {
        conn = connection;
        generator = new DataRecordGenerator();
        r = new Random();
    }

    public void execute() {
        final float newCustomersInsertShare = r.nextInt(4, 8) * 0.01F;
        final float existingCustomersUpdateShare = r.nextInt(9, 17) * 0.01F;

        int target = SimulatorConfig.RECORDS_PER_CYCLE;
        int newProductsCount = 0;
        int newCustomersCount = 0;
        int newOrdersWithOrderItemsCount = 0;
        int existingCustomersUpdateCount = 0;

        try {
            final List<BatchProcessor<?>> batchProcessors = new ArrayList<>();

            final int productsAvailable = generator.getProductsCount();
            final int productsCanBeGenerated = SimulatorConfig.PRODUCT_COUNT_CAP - productsAvailable;
            if(productsCanBeGenerated >= 1) {
                newProductsCount = r.nextInt(1, productsCanBeGenerated + 1);
                batchProcessors.add(this.getBatchForInsertingNewProducts(newProductsCount));
                target -= newProductsCount;
            }

            newCustomersCount = (int) (target * newCustomersInsertShare);
            batchProcessors.add(this.getBatchForInsertingNewCustomers(newCustomersCount));

            existingCustomersUpdateCount = (int) (target * existingCustomersUpdateShare);
            batchProcessors.add(this.getBatchForUpdatingExistingCustomers(existingCustomersUpdateCount));

            newOrdersWithOrderItemsCount = target - newCustomersCount - existingCustomersUpdateCount;
            batchProcessors.addAll(this.getBatchesForInsertingOrdersAndOrderItems(newOrdersWithOrderItemsCount));

            this.executeBatches(batchProcessors);
        } catch (final Exception e) {
            e.printStackTrace();
            newProductsCount = newCustomersCount = newOrdersWithOrderItemsCount = existingCustomersUpdateCount = 0;
        } finally {
            final int totalRecords = newCustomersCount + newProductsCount + newOrdersWithOrderItemsCount + existingCustomersUpdateCount;
            totalOperationDone += totalRecords;

            System.out.printf(
                "========================================================\n" +
                "Summary:\nNew customer records inserted: %d\nNew products inserted: %d\n" +
                "New orders & order items inserted: %d\nExisting customer records updated: %d\n" +
                "Total records inserted/updated in this batch: %d\nOverall insertion & updation operation performed: %d\n" +
                "========================================================\n",
                newCustomersCount, newProductsCount, newOrdersWithOrderItemsCount,
                existingCustomersUpdateCount, totalRecords, totalOperationDone
            );
        }
    }

    private void executeBatches(final List<BatchProcessor<?>> batchProcessors) throws SQLException {
        for(final BatchProcessor<?> batchProcessor : batchProcessors) {
            if(batchProcessor.getRecordsCount() == 0) {
                continue;
            }
            batchProcessor.execute(this.conn);
        }
        this.conn.commit();
    }

    private CustomerTableInsertBatchProcessor getBatchForInsertingNewCustomers(final int generationCount) {
        boolean newCustomerGenerated = false;

        final List<CustomerModel> newCustomers = new ArrayList<>();
        for(int i = 0; i < generationCount; ++i) {
            if(generator.getCustomersCount() == SimulatorConfig.CUSTOMER_COUNT_CAP) {
                break;
            }
            newCustomerGenerated = true;
            newCustomers.add(generator.getNewCustomer());
        }
        if(!newCustomerGenerated) {
            System.out.println("Customer record generation limit reached. No more generation for customer table occurs.");
            return new CustomerTableInsertBatchProcessor(Collections.emptyList());
        }

        return new CustomerTableInsertBatchProcessor(newCustomers);
    }

    private CustomerTableUpdateBatchProcessor getBatchForUpdatingExistingCustomers(final int generationCount) {
        final List<CustomerModel> existingCustomers = new ArrayList<>();
        for(int i = 0; i < generationCount; ++i) {
            existingCustomers.add(generator.getUpdatedCustomer());
        }

       return new CustomerTableUpdateBatchProcessor(existingCustomers);
    }

    private ProductTableInsertBatchProcessor getBatchForInsertingNewProducts(final int generationCount) {
        boolean newProductGenerated = false;

        final List<ProductModel> newProducts = new ArrayList<>();
        for(int i = 0; i < generationCount; ++i) {
            if(generator.getProductsCount() == SimulatorConfig.PRODUCT_COUNT_CAP) {
                break;
            }
            newProductGenerated = true;
            newProducts.add(generator.getNewProduct());
        }
        if(!newProductGenerated) {
            System.out.println("Product record generation limit reached. No more generation for products table occurs.");
            return new ProductTableInsertBatchProcessor(Collections.emptyList());
        }

       return new ProductTableInsertBatchProcessor(newProducts);
    }

    private List<BatchProcessor<?>> getBatchesForInsertingOrdersAndOrderItems(int generationCount) {
        boolean newOrderGenerated = false;

        final List<OrderModel> newOrders = new ArrayList<>();
        final List<OrderItemModel> newOrderItems = new ArrayList<>();

        while(generationCount > 0) {
            int orderItems;

            if(generationCount < 5 && generationCount <= generator.getProductsCount()) {
                orderItems = generationCount - 1;
                generationCount = 0;
            } else {
                orderItems = r.nextInt(1, generator.getProductsCount() + 1);
                generationCount -= orderItems + 1;
            }

            final OrderModel order = generator.getNewOrder();
            newOrders.add(order);

            if(orderItems > 0) {
                newOrderGenerated = true;
            }
            newOrderItems.addAll(generator.getNewOrderItemsForOrder(order.getOrderId(), orderItems));
        }

        if(!newOrderGenerated) {
            System.out.println("Order record generation limit reached. No more generation for orders table occurs.");
            return new ArrayList<>();
        }

        final BatchProcessor<OrderModel> newOrderInsertOperation = new OrderTableInsertBatchProcessor(newOrders);
        final BatchProcessor<OrderItemModel> newOrderItemInsertOperation = new OrderItemTableInsertBatchProcessor(newOrderItems);

        return List.of(newOrderInsertOperation, newOrderItemInsertOperation);
    }

}
