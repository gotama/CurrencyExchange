package com.gautamastudios.currencyexchange.services.jobs;

import com.path.android.jobqueue.Params;

import com.gautamastudios.currencyexchange.enums.JobQueuePriority;
import com.gautamastudios.currencyexchange.events.GetSupportedCurrenciesFailed;
import com.gautamastudios.currencyexchange.events.GetSupportedCurrenciesFinished;
import com.gautamastudios.currencyexchange.services.ExchangeRateService;

public class GetSupportedCurrenciesJob extends BaseInjectorJob {

    private final int MAX_RETRY_LIMIT = 3;
    private boolean mDatabaseCall;

    public GetSupportedCurrenciesJob(boolean databaseCall) {
        super(new Params(JobQueuePriority.HIGH.getId()).requireNetwork());
        mDatabaseCall = databaseCall;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        ExchangeRateService service = new ExchangeRateService();
        mCurrenciesBroker.saveSupportedCurrencies(service.getSupportedCurrencies());

        mEventBus.post(new GetSupportedCurrenciesFinished(mDatabaseCall));
    }

    @Override
    protected void onCancel() {
        mEventBus.post(new GetSupportedCurrenciesFailed());
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return true;
    }

    @Override
    protected int getRetryLimit() {
        return MAX_RETRY_LIMIT;
    }
}
