package com.gautamastudios.currencyexchange.events;

import com.gautamastudios.currencyexchange.models.SupportedCurrenciesModel;

public class ViewPagerPageChange {

    boolean mClearEditText;
    boolean isCurrentCurrency;
    SupportedCurrenciesModel mCompareCurrencyModel;

    public ViewPagerPageChange(SupportedCurrenciesModel compareCurrencyModel, boolean isCurrentCurrency, boolean clearEditText) {
        this.mCompareCurrencyModel = compareCurrencyModel;
        this.isCurrentCurrency = isCurrentCurrency;
        this.mClearEditText = clearEditText;
    }

    public SupportedCurrenciesModel getCompareCurrencyModel() {
        return mCompareCurrencyModel;
    }

    public boolean isCurrentCurrency() {
        return isCurrentCurrency;
    }

    public boolean isClearEditText() {
        return mClearEditText;
    }
}
