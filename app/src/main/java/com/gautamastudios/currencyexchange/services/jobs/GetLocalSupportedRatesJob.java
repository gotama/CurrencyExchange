package com.gautamastudios.currencyexchange.services.jobs;

import com.path.android.jobqueue.Params;

import com.gautamastudios.currencyexchange.enums.JobQueuePriority;
import com.gautamastudios.currencyexchange.events.GetSupportedRatesFailed;
import com.gautamastudios.currencyexchange.events.GetSupportedRatesFinished;

public class GetLocalSupportedRatesJob extends BaseInjectorJob {

    private final int MAX_RETRY_LIMIT = 3;

    public GetLocalSupportedRatesJob() {
        super(new Params(JobQueuePriority.HIGH.getId()).requireNetwork());
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        mEventBus.post(new GetSupportedRatesFinished(mCurrenciesBroker.getAllSupportedCurrencies()));
    }

    @Override
    protected void onCancel() {
        mEventBus.post(new GetSupportedRatesFailed());
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
