package org.ajikhoji.dripfeed.config;

public final class SimulatorConfig {

    private SimulatorConfig() { }

    public static final int RECORDS_PER_SECOND = 834;
    public static final int INTERVAL_SECONDS = 15;

    public static final int RECORDS_PER_CYCLE = RECORDS_PER_SECOND * INTERVAL_SECONDS;

    public static final int PRODUCT_COUNT_CAP = 800;
    public static final int CUSTOMER_COUNT_CAP = Integer.MAX_VALUE - 4;
    public static final int ORDER_COUNT_CAP = Integer.MAX_VALUE - 4;

}
