package com.gautamastudios.currencyexchange;

import android.app.Application;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import com.gautamastudios.currencyexchange.modules.AndroidModule;
import com.gautamastudios.currencyexchange.modules.BrokerModule;
import com.gautamastudios.currencyexchange.modules.RepositoryModule;
import com.gautamastudios.currencyexchange.modules.ServiceModule;
import com.gautamastudios.currencyexchange.interfaces.ApplicationComponent;
import com.gautamastudios.currencyexchange.interfaces.DaggerApplicationComponent;
import com.gautamastudios.currencyexchange.interfaces.EventHandler;

public class CurrencyExchangeApplication extends Application {

    protected static WeakReference<CurrencyExchangeApplication> mApplication;
    private ApplicationComponent mApplicationComponent;

    @Inject
    public List<EventHandler> mEventHandlerList;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = new WeakReference<CurrencyExchangeApplication>(this);

        mApplicationComponent = DaggerApplicationComponent.builder()
                .androidModule(new AndroidModule(this))
                .serviceModule(new ServiceModule())
                .repositoryModule(new RepositoryModule())
                .brokerModule(new BrokerModule())
                .build();

        applicationComponent().inject(this);

        for (EventHandler eventHandler : mEventHandlerList) {
            eventHandler.register(this);
        }
    }

    public ApplicationComponent applicationComponent() {
        return mApplicationComponent;
    }


    public static CurrencyExchangeApplication getApplication() {
        return mApplication.get();
    }

}
