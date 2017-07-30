package com.gautamastudios.currencyexchange.events;


public class GetSupportedCurrenciesFinished {
    private boolean mDatabaseCall;

    public GetSupportedCurrenciesFinished(boolean databaseCall) {
        this.mDatabaseCall = databaseCall;
    }

    public boolean isDatabaseCall() {
        return mDatabaseCall;
    }
}
