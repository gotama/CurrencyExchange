package com.gautamastudios.currencyexchange.modules;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import com.gautamastudios.currencyexchange.interfaces.ForORMLiteDatabase;
import com.gautamastudios.currencyexchange.repositories.ORMDatabaseHelper;
import com.gautamastudios.currencyexchange.repositories.SupportedCurrenciesRepository;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    @ForORMLiteDatabase
    ORMDatabaseHelper provideDatabaseHelper(Application application) {
        return new ORMDatabaseHelper(application);
    }

    @Provides
    @Singleton
    SupportedCurrenciesRepository provideConfigurationRepository(@ForORMLiteDatabase ORMDatabaseHelper repositoryHelper, Application application) {
        return new SupportedCurrenciesRepository(application, repositoryHelper);
    }
}
