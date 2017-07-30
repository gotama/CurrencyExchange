package com.gautamastudios.currencyexchange.interfaces;

public interface Repository {
    void onCreate();
    void onUpgrade(int oldVersion, int newVersion);
    boolean getWasRecentlyUpgraded();
}
