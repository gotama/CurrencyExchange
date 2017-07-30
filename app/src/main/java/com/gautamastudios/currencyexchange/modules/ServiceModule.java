package com.gautamastudios.currencyexchange.modules;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;
import com.gautamastudios.currencyexchange.events.GreenRobotEventBus;
import com.gautamastudios.currencyexchange.events.handlers.CurrenciesHandler;
import com.gautamastudios.currencyexchange.interfaces.EventHandler;
import com.gautamastudios.currencyexchange.services.PathJobQueue;

@Module
public class ServiceModule {

    @Provides
    @Singleton
    PathJobQueue provideJobQueue(Application application) {
        return new PathJobQueue(application);
    }

    @Provides
    @Singleton
    GreenRobotEventBus provideEventBus(Application application) {
        return new GreenRobotEventBus(application, EventBus.getDefault());
    }

    @Provides
    @Singleton
    public List<EventHandler> provideEventHandlerList(GreenRobotEventBus eventBus, PathJobQueue jobQueue) {
        List<EventHandler> retVal = new ArrayList<EventHandler>();

        retVal.add(new CurrenciesHandler(eventBus, jobQueue));

        return retVal;
    }
}
