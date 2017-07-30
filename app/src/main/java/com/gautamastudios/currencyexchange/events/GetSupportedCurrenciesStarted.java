package com.gautamastudios.currencyexchange.events;

public class GetSupportedCurrenciesStarted {
    private boolean mDatabaseCall;

    public GetSupportedCurrenciesStarted(boolean databaseCall) {
        this.mDatabaseCall = databaseCall;
    }

    public boolean isDatabaseCall() {
        return mDatabaseCall;
    }
}
