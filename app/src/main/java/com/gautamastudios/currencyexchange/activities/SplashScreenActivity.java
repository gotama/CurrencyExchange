package com.gautamastudios.currencyexchange.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import javax.inject.Inject;

import com.gautamastudios.currencyexchange.R;
import com.gautamastudios.currencyexchange.CurrencyExchangeApplication;
import com.gautamastudios.currencyexchange.events.GetSupportedCurrenciesStarted;
import com.gautamastudios.currencyexchange.events.GreenRobotEventBus;

public class SplashScreenActivity extends Activity {

    private static int SPLASH_TIME_OUT = 1500;

    @Inject
    GreenRobotEventBus mEventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((CurrencyExchangeApplication) getApplication()).applicationComponent().inject(this);

        mEventBus.post(new GetSupportedCurrenciesStarted(false));

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
