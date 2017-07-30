package com.gautamastudios.currencyexchange.modules;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import com.gautamastudios.currencyexchange.CurrencyExchangeApplication;

@Module
public class AndroidModule {
    Application mApplication;

    public AndroidModule(CurrencyExchangeApplication application) {
        this.mApplication = application;
    }

    @Provides
    @Singleton
    Application provideApplicationContext() {
        return mApplication;
    }
}
