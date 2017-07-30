package com.gautamastudios.currencyexchange.services.jobs;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

import com.gautamastudios.currencyexchange.brokers.CurrenciesBroker;
import com.gautamastudios.currencyexchange.events.GreenRobotEventBus;

public class BaseInjectorJob extends Job {

    @Inject
    protected GreenRobotEventBus mEventBus;

    @Inject
    protected CurrenciesBroker mCurrenciesBroker;

    public BaseInjectorJob(Params params) {
        super(params);
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {

    }

    @Override
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
