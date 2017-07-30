package com.gautamastudios.currencyexchange.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.gautamastudios.currencyexchange.R;
import com.gautamastudios.currencyexchange.enums.FragmentEnum;
import com.gautamastudios.currencyexchange.events.CalculateAmountFinished;
import com.gautamastudios.currencyexchange.events.CalculateAmountStarted;
import com.gautamastudios.currencyexchange.events.GetSupportedCurrenciesStarted;
import com.gautamastudios.currencyexchange.events.GetSupportedRatesFinished;
import com.gautamastudios.currencyexchange.events.ViewPagerPageChange;
import com.gautamastudios.currencyexchange.interfaces.PageIndicator;
import com.gautamastudios.currencyexchange.models.RatesModel;
import com.gautamastudios.currencyexchange.models.SupportedCurrenciesModel;
import com.gautamastudios.currencyexchange.utils.AmountsToDisplayUtil;
import com.gautamastudios.currencyexchange.utils.CalculationUtil;
import com.gautamastudios.currencyexchange.viewpageindicator.CirclePageIndicator;

public class ExchangeFragment extends BaseFragment {

    private Timer timer;
    private TimerTask timerTask;

    private ViewPager mCurrentViewPager;
    private ViewPager mDisplayViewPager;

    List<SupportedCurrenciesModel> mSupportedCurrenciesModelList;

    PageIndicator mIndicator;
    PageIndicator mIndicator2;

    private View mView;

    String mAmountToCalculated = "0.00";

    public static ExchangeFragment newInstance() {
        ExchangeFragment fragment = new ExchangeFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        register();
        try {
            mSupportedCurrenciesModelList = mBroker.getAllSupportedCurrencies();
        } catch (Exception e) {
            mSupportedCurrenciesModelList = new ArrayList<>();
            e.printStackTrace();
        }
        System.err.println("onCreate Running");
        mEventBus.post(new GetSupportedCurrenciesStarted(true));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_exchange, container, false);
        }
        drawUIControllers();
        return mView;
    }

    public void onResume() {
        super.onResume();
        try {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    System.err.println("TimerTask Running");
                    mEventBus.post(new GetSupportedCurrenciesStarted(true));

                }
            };
            timer.schedule(timerTask, 30000, 30000);
        } catch (IllegalStateException e) {

        }
    }

    public void onPause() {
        timer.cancel();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        unregister();
        super.onDestroy();
    }

    private void getAmountedCalculated(String value, SupportedCurrenciesModel model) {
        boolean isRateAvailable = false;
        for (RatesModel rate : model.getRates()) {
            if (rate.getCurrencyCode().equals(mSupportedCurrenciesModelList.get(mDisplayViewPager.getCurrentItem()).getCurrencyCode())) {
                mEventBus.post(new CalculateAmountFinished(
                        CalculationUtil.multipleIntValueByRateInt(
                        CalculationUtil.getIntFromStringValue(value), rate.getRate()), mSupportedCurrenciesModelList.get(mCurrentViewPager.getCurrentItem())));
                isRateAvailable = true;
                break;
            }
        }

        if (!isRateAvailable) {
            mEventBus.post(new CalculateAmountFinished(CalculationUtil.multipleIntValueByRateInt(
                    CalculationUtil.getIntFromStringValue(value), 100000), mSupportedCurrenciesModelList.get(mCurrentViewPager.getCurrentItem())));
        }
    }

    public void onEventMainThread(CalculateAmountStarted e) {
        mAmountToCalculated = e.getAmountToCalculate();
        getAmountedCalculated(mAmountToCalculated, mSupportedCurrenciesModelList.get(mCurrentViewPager.getCurrentItem()));
    }

    public void onEventMainThread(GetSupportedRatesFinished e) {
        mSupportedCurrenciesModelList = e.getSupportedCurrenciesModelList();
        drawUIControllers();

        if (mSupportedCurrenciesModelList.size() > 0) {
            mEventBus.post(new ViewPagerPageChange(mSupportedCurrenciesModelList.get(mCurrentViewPager.getCurrentItem()), true, false));
        }

    }

    public void onEventMainThread(ViewPagerPageChange e) {
        TextView unitOneTextView = (TextView) mView.findViewById(R.id.unit_one_text_view);
        TextView currentExchangeTextView = (TextView) mView.findViewById(R.id.current_exchange_text_view);
        TextView currentExchangeCentsTextView = (TextView) mView.findViewById(R.id.current_exchange_cents_text_view);

        unitOneTextView.setText(String.format("%s = ", AmountsToDisplayUtil.displayAmountWithCurrency("100", e.getCompareCurrencyModel().getCurrencyCode(), true)));

        SupportedCurrenciesModel supportedCurrenciesModel = mSupportedCurrenciesModelList.get(mDisplayViewPager.getCurrentItem());
        List<RatesModel> modelList = e.getCompareCurrencyModel().getRates();
        boolean isRateAvailable = false;
        for (RatesModel model : modelList) {
            if (model.getCurrencyCode().equals(supportedCurrenciesModel.getCurrencyCode())) {
                String amount = AmountsToDisplayUtil.displayAmountWithCurrency(String.valueOf(model.getRate()), model.getCurrencyCode(), false);
                String smallAmount = amount.substring(amount.length() - 2, amount.length());
                amount = amount.substring(0, amount.length() - 2);
                currentExchangeTextView.setText(amount);
                currentExchangeCentsTextView.setText(smallAmount);
                isRateAvailable = true;
            }
        }

        if (!isRateAvailable) {
            currentExchangeTextView.setText(AmountsToDisplayUtil.displayAmountWithCurrency("100", supportedCurrenciesModel.getCurrencyCode(), true));
            currentExchangeCentsTextView.setText("00");
        }

        getAmountedCalculated(mAmountToCalculated, mSupportedCurrenciesModelList.get(mCurrentViewPager.getCurrentItem()));

    }

    private void drawUIControllers() {

        mCurrentViewPager = (ViewPager) mView.findViewById(R.id.first_currency_view_pager);
        mCurrentViewPager.setAdapter(new CurrentPagerAdapter(getActivity().getSupportFragmentManager(), mSupportedCurrenciesModelList));
        mCurrentViewPager.addOnPageChangeListener(CurrentPageChangeListener);
        mCurrentViewPager.setCurrentItem(currentExposedPosition);

        mDisplayViewPager = (ViewPager) mView.findViewById(R.id.second_currency_view_pager);
        mDisplayViewPager.setAdapter(new DisplayPagerAdapter(getActivity().getSupportFragmentManager(), mSupportedCurrenciesModelList));
        mDisplayViewPager.addOnPageChangeListener(DisplayPageChangeListener);
        mDisplayViewPager.setCurrentItem(displayedExposedPosition);

        mIndicator = (CirclePageIndicator) mView.findViewById(R.id.first_indicator);
        mIndicator.setViewPager(mCurrentViewPager);

        mIndicator2 = (CirclePageIndicator) mView.findViewById(R.id.second_indicator);
        mIndicator2.setViewPager(mDisplayViewPager);


    }

    @Override
    public FragmentEnum getFragmentEnum() {
        return FragmentEnum.EXCHANGE_FRAGMENT;
    }

    public static class CurrentPagerAdapter extends FragmentPagerAdapter {
        private List<SupportedCurrenciesModel> mSupportedCurrenciesModels;

        public CurrentPagerAdapter(FragmentManager fragmentManager, List<SupportedCurrenciesModel> supportedCurrenciesModels) {
            super(fragmentManager);
            mSupportedCurrenciesModels = supportedCurrenciesModels;
        }

        @Override
        public int getCount() {
            return mSupportedCurrenciesModels.size();
        }

        @Override
        public Fragment getItem(int position) {
            return CurrentCurrencySlideFragment.newInstance(mSupportedCurrenciesModels.get(position));
        }

    }

    public static class DisplayPagerAdapter extends FragmentPagerAdapter {
        private List<SupportedCurrenciesModel> mSupportedCurrenciesModels;

        public DisplayPagerAdapter(FragmentManager fragmentManager, List<SupportedCurrenciesModel> supportedCurrenciesModels) {
            super(fragmentManager);
            mSupportedCurrenciesModels = supportedCurrenciesModels;
        }

        @Override
        public int getCount() {
            return mSupportedCurrenciesModels.size();
        }

        @Override
        public Fragment getItem(int position) {
            return DisplayCurrencySlideFragment.newInstance(mSupportedCurrenciesModels.get(position));
        }

    }

    int currentExposedPosition;
    public ViewPager.OnPageChangeListener CurrentPageChangeListener = new ViewPager.OnPageChangeListener() {

        private int previousPosition;

        public void setPreviousPosition(int position) {
            currentExposedPosition = position;
            previousPosition = position;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (position != previousPosition) {
                mEventBus.post(new ViewPagerPageChange(mSupportedCurrenciesModelList.get(position), true, true));
                setPreviousPosition(mCurrentViewPager.getCurrentItem());
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };


    int displayedExposedPosition;
    public ViewPager.OnPageChangeListener DisplayPageChangeListener = new ViewPager.OnPageChangeListener() {

        private int previousPosition;

        public void setPreviousPosition(int position) {
            displayedExposedPosition = position;
            previousPosition = position;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (position != previousPosition) {
                mEventBus.post(new ViewPagerPageChange(mSupportedCurrenciesModelList.get(mCurrentViewPager.getCurrentItem()), false, false));
                setPreviousPosition(mDisplayViewPager.getCurrentItem());
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };


    public void register() {
        if (!mEventBus.isRegistered(this)) {
            mEventBus.register(this);
        }
    }

    public void unregister() {
        this.mEventBus.unregister(this);
    }

}
