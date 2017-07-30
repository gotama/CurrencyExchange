package com.gautamastudios.currencyexchange.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.util.List;

import javax.inject.Inject;

import com.gautamastudios.currencyexchange.R;
import com.gautamastudios.currencyexchange.CurrencyExchangeApplication;
import com.gautamastudios.currencyexchange.events.GreenRobotEventBus;
import com.gautamastudios.currencyexchange.fragments.BaseFragment;
import com.gautamastudios.currencyexchange.fragments.LandingFragment;
import com.gautamastudios.currencyexchange.interfaces.ActivityInterface;
import com.gautamastudios.currencyexchange.interfaces.EventHandler;

public class MainActivity extends FragmentActivity implements ActivityInterface {

    @Inject
    GreenRobotEventBus mEventBus;

    @Inject
    public List<EventHandler> eventHandlerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeFragment(LandingFragment.newInstance());

        ((CurrencyExchangeApplication) getApplication()).applicationComponent().inject(this);

        for (EventHandler eventHandler : eventHandlerList) {
            eventHandler.bindContext(this);
        }
    }

    @Override
    public void changeFragment(BaseFragment baseFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activity_container, baseFragment)
                .addToBackStack(baseFragment.getFragmentEnum().getFragmentTag())
                .commit();
    }
}
