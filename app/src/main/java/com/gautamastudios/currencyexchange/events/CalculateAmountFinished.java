package com.gautamastudios.currencyexchange.events;

import java.math.BigDecimal;

import com.gautamastudios.currencyexchange.models.SupportedCurrenciesModel;

public class CalculateAmountFinished {

    BigDecimal mTotalAmount;
    SupportedCurrenciesModel mCompareCurrencyModel;

    public CalculateAmountFinished(BigDecimal totalAmount, SupportedCurrenciesModel compareCurrencyModel) {
        this.mTotalAmount = totalAmount;
        this.mCompareCurrencyModel = compareCurrencyModel;
    }

    public BigDecimal getTotalAmount() {
        return mTotalAmount;
    }

    public SupportedCurrenciesModel getCompareCurrencyModel() {
        return mCompareCurrencyModel;
    }

}
