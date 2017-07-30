package com.gautamastudios.currencyexchange.events;

public class GetSupportedRatesStarted {
    private boolean mDatabaseCall;

    public GetSupportedRatesStarted(boolean databaseCall) {
        this.mDatabaseCall = databaseCall;
    }

    public boolean isDatabaseCall() {
        return mDatabaseCall;
    }
}
