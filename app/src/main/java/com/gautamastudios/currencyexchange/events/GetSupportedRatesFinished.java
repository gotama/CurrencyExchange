package com.gautamastudios.currencyexchange.events;

import java.util.List;

import com.gautamastudios.currencyexchange.models.SupportedCurrenciesModel;

public class GetSupportedRatesFinished {
    private List<SupportedCurrenciesModel> mSupportedCurrenciesModelList;

    public GetSupportedRatesFinished(List<SupportedCurrenciesModel> supportedCurrenciesModelList) {
        this.mSupportedCurrenciesModelList = supportedCurrenciesModelList;
    }

    public List<SupportedCurrenciesModel> getSupportedCurrenciesModelList() {
        return mSupportedCurrenciesModelList;
    }
}
