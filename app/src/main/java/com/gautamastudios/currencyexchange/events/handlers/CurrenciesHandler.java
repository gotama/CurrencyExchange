package com.gautamastudios.currencyexchange.events.handlers;

import android.content.Context;

import com.gautamastudios.currencyexchange.CurrencyExchangeApplication;
import com.gautamastudios.currencyexchange.events.GetSupportedCurrenciesFinished;
import com.gautamastudios.currencyexchange.events.GetSupportedCurrenciesStarted;
import com.gautamastudios.currencyexchange.events.GetSupportedRatesStarted;
import com.gautamastudios.currencyexchange.events.GreenRobotEventBus;
import com.gautamastudios.currencyexchange.interfaces.EventHandler;
import com.gautamastudios.currencyexchange.services.PathJobQueue;
import com.gautamastudios.currencyexchange.services.jobs.GetLocalSupportedRatesJob;
import com.gautamastudios.currencyexchange.services.jobs.GetSupportedCurrenciesJob;
import com.gautamastudios.currencyexchange.services.jobs.GetSupportedRatesJob;

public class CurrenciesHandler implements EventHandler {

    private Context mContext;
    private PathJobQueue mJobQueue;
    private GreenRobotEventBus mEventBus;

    public CurrenciesHandler(GreenRobotEventBus eventBus, PathJobQueue jobQueue) {
        this.mJobQueue = jobQueue;
        this.mEventBus = eventBus;
    }

    public void onEvent(GetSupportedCurrenciesStarted e) {
        mJobQueue.addJobInBackground(new GetSupportedCurrenciesJob(e.isDatabaseCall()));
    }

    public void onEvent(GetSupportedCurrenciesFinished e) {
        mEventBus.post(new GetSupportedRatesStarted(e.isDatabaseCall()));
    }

    public void onEvent(GetSupportedRatesStarted e) {
        if (e.isDatabaseCall()) {
            mJobQueue.addJobInBackground(new GetLocalSupportedRatesJob());
        }
        mJobQueue.addJobInBackground(new GetSupportedRatesJob());
    }

    @Override
    public void register(Context context) {
        mContext = context;
        if (!mEventBus.isRegistered(this)) {
            mEventBus.register(this);
        }
    }

    @Override
    public void bindContext(Context context) {
        mContext = context;
    }

    @Override
    public void unbindContext() {
        mContext = CurrencyExchangeApplication.getApplication();
    }

    @Override
    public void unregister() {
        if (mEventBus.isRegistered(this)) {
            mEventBus.unregister(this);
        }
    }
}
