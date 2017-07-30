package com.gautamastudios.currencyexchange.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;

import javax.inject.Inject;

import com.gautamastudios.currencyexchange.CurrencyExchangeApplication;
import com.gautamastudios.currencyexchange.brokers.CurrenciesBroker;
import com.gautamastudios.currencyexchange.enums.FragmentEnum;
import com.gautamastudios.currencyexchange.events.GreenRobotEventBus;
import com.gautamastudios.currencyexchange.interfaces.ActivityInterface;

public abstract class BaseFragment extends Fragment {

    protected ActivityInterface mActivityInterface;

    @Inject
    GreenRobotEventBus mEventBus;

    @Inject
    CurrenciesBroker mBroker;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mActivityInterface = (ActivityInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ActivityInterface");
        }

        ((CurrencyExchangeApplication) context.getApplicationContext()).applicationComponent().inject(this);
    }

    public abstract FragmentEnum getFragmentEnum();
}
