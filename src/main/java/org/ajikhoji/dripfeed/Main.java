package org.ajikhoji.dripfeed;

import org.ajikhoji.dripfeed.database.DatabaseManager;
import org.ajikhoji.dripfeed.orchestrator.Orchestrator;
import org.ajikhoji.dripfeed.scheduler.DripFeedScheduler;

import java.sql.SQLException;

public class Main {

    private void launch() {
        final DatabaseManager databaseManager = DatabaseManager.getInstance();

        try {
            databaseManager.getConnection().setAutoCommit(false);
        } catch (final SQLException sqlException) {
            System.err.println("Unable to turn off auto-commit");
            System.exit(-1);
        }

        final Orchestrator orchestrator = new Orchestrator(databaseManager.getConnection());
        final DripFeedScheduler scheduler = new DripFeedScheduler(orchestrator::execute);
        scheduler.start();
    }

    public static void main(String[] args) {
        final Main m = new Main();
        m.launch();
    }

}