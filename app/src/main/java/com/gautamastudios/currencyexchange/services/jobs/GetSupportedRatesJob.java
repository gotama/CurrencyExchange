package com.gautamastudios.currencyexchange.services.jobs;

import com.path.android.jobqueue.Params;

import java.net.SocketTimeoutException;
import java.util.List;

import retrofit2.Response;
import com.gautamastudios.currencyexchange.enums.JobQueuePriority;
import com.gautamastudios.currencyexchange.events.GetSupportedRatesFailed;
import com.gautamastudios.currencyexchange.events.GetSupportedRatesFinished;
import com.gautamastudios.currencyexchange.models.SupportedCurrenciesModel;
import com.gautamastudios.currencyexchange.services.ExchangeRateService;

public class GetSupportedRatesJob extends BaseInjectorJob {

    private final int MAX_RETRY_LIMIT = 3;
    private boolean mUseFallBackAPI;

    public GetSupportedRatesJob() {
        super(new Params(JobQueuePriority.HIGH.getId()).requireNetwork());
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {

        List<SupportedCurrenciesModel> currenciesModelList = mCurrenciesBroker.getAllSupportedCurrencies();
        ExchangeRateService service = new ExchangeRateService();
        for (SupportedCurrenciesModel model : currenciesModelList) {
            Response<Object> response;

            response = service.getSpecificRate("USD,GBP,EUR", model.getCurrencyCode());
            mCurrenciesBroker.saveSupportedRate(response, mUseFallBackAPI, model.getId());
        }

        mEventBus.post(new GetSupportedRatesFinished(mCurrenciesBroker.getAllSupportedCurrencies()));
    }

    @Override
    protected void onCancel() {
        mEventBus.post(new GetSupportedRatesFailed());
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        if (throwable instanceof SocketTimeoutException) {
            mUseFallBackAPI = true;
        } else {
            mUseFallBackAPI = false;
        }
        return true;
    }

    @Override
    protected int getRetryLimit() {
        return MAX_RETRY_LIMIT;
    }
}
