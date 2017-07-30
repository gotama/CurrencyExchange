package com.gautamastudios.currencyexchange.interfaces;

import javax.inject.Singleton;

import dagger.Component;
import com.gautamastudios.currencyexchange.CurrencyExchangeApplication;
import com.gautamastudios.currencyexchange.activities.MainActivity;
import com.gautamastudios.currencyexchange.activities.SplashScreenActivity;
import com.gautamastudios.currencyexchange.modules.AndroidModule;
import com.gautamastudios.currencyexchange.modules.BrokerModule;
import com.gautamastudios.currencyexchange.modules.RepositoryModule;
import com.gautamastudios.currencyexchange.modules.ServiceModule;
import com.gautamastudios.currencyexchange.fragments.BaseFragment;
import com.gautamastudios.currencyexchange.services.jobs.BaseInjectorJob;

@Singleton
@Component(modules = {
        AndroidModule.class,
        ServiceModule.class,
        RepositoryModule.class,
        BrokerModule.class
})
public interface ApplicationComponent {
    void inject(CurrencyExchangeApplication application);

    void inject(MainActivity activity);

    void inject(SplashScreenActivity activity);

    void inject(BaseInjectorJob job);

    void inject(BaseFragment fragment);
}
