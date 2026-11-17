package org.ajikhoji.dripfeed.processor;

import java.sql.Connection;
import java.sql.SQLException;

public interface BatchProcessor<T> {

    void execute(Connection connection) throws SQLException;

    int getRecordsCount();

}
