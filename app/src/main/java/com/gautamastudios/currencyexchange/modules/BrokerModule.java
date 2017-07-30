package com.gautamastudios.currencyexchange.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import com.gautamastudios.currencyexchange.brokers.CurrenciesBroker;
import com.gautamastudios.currencyexchange.repositories.SupportedCurrenciesRepository;

@Module
public class BrokerModule {

    @Provides
    @Singleton
    CurrenciesBroker provideCurrenciesBroker(SupportedCurrenciesRepository currenciesRepository) {
        return new CurrenciesBroker(currenciesRepository);
    }
}
