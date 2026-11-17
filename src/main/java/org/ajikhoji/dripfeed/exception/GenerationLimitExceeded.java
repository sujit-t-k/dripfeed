package org.ajikhoji.dripfeed.exception;

public class GenerationLimitExceeded extends RuntimeException {

    public GenerationLimitExceeded(final String limitExceededFor) {
        super(String.format("New record generation limit for %s has been reached. Cannot generate anymore records.", limitExceededFor));
    }

}
