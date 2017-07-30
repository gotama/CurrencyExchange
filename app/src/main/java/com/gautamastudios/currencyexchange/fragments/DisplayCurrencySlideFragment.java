package com.gautamastudios.currencyexchange.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.gautamastudios.currencyexchange.R;
import com.gautamastudios.currencyexchange.enums.FragmentEnum;
import com.gautamastudios.currencyexchange.events.CalculateAmountFinished;
import com.gautamastudios.currencyexchange.events.ViewPagerPageChange;
import com.gautamastudios.currencyexchange.models.RatesModel;
import com.gautamastudios.currencyexchange.models.SupportedCurrenciesModel;
import com.gautamastudios.currencyexchange.utils.AmountsToDisplayUtil;

public class DisplayCurrencySlideFragment extends BaseFragment {
    public static final String SUPPORTED_CURRENCY = "supported_currency";
    private SupportedCurrenciesModel mSupportedCurrency;
    private View mView;
    private TextView mAmountTextView;
    private TextView mCurrencyTextView;
    private TextView mCustomAmountTextView;
    private TextView mRatesTextView;
    private String mAmountToDisplay = "";

    public void register() {
        if (!mEventBus.isRegistered(this)) {
            mEventBus.register(this);
        }
    }

    public void unregister() {
        this.mEventBus.unregister(this);
    }

    public void onEventMainThread(ViewPagerPageChange e) {
        drawUI(e.getCompareCurrencyModel().getCurrencyCode());
    }

    public void onEventMainThread(CalculateAmountFinished e) {
        mAmountToDisplay = AmountsToDisplayUtil.displayBigDecimalWithCurrency(e.getTotalAmount(), mSupportedCurrency.getCurrencyCode());
        drawUI(e.getCompareCurrencyModel().getCurrencyCode());
    }

    public static DisplayCurrencySlideFragment newInstance(SupportedCurrenciesModel supportedCurrenciesModel) {
        DisplayCurrencySlideFragment fragment = new DisplayCurrencySlideFragment();
        Bundle args = new Bundle();
        args.putSerializable(SUPPORTED_CURRENCY, supportedCurrenciesModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        register();
        mSupportedCurrency = (SupportedCurrenciesModel) getArguments().getSerializable(SUPPORTED_CURRENCY);
    }

    @Override
    public void onDestroy() {
        unregister();
        super.onDestroy();
    }

    private void drawUI(String currencyCode) {
        if (mCurrencyTextView == null) {
            mCurrencyTextView = (TextView) mView.findViewById(R.id.currency_name_text_view);
        }
        mCurrencyTextView.setText(mSupportedCurrency.getCurrencyCode());

        if (mCustomAmountTextView == null) {
            mCustomAmountTextView = (TextView) mView.findViewById(R.id.customer_amount_text_view);
        }
        mCustomAmountTextView.setText(getString(R.string.default_own_amount));

        if (mRatesTextView == null) {
            mRatesTextView = (TextView) mView.findViewById(R.id.currency_exchange_rate);
        }

        List<RatesModel> rates = mSupportedCurrency.getRates();
        boolean isRateAvailable = false;
        for (RatesModel model : rates) {
            if (model.getCurrencyCode().equals(currencyCode)) {
                mRatesTextView.setText(AmountsToDisplayUtil.displayAmountWithCurrency(String.valueOf(model.getRate()), model.getCurrencyCode(), false));
                isRateAvailable = true;
            }
        }

        if (!isRateAvailable) {
            mRatesTextView.setText("");
        }

        mAmountTextView = (TextView) mView.findViewById(R.id.amount_text_view);
        mAmountTextView.setText(mAmountToDisplay);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_currency_slide, container, false);
        }
        drawUI("");
        return mView;
    }

    @Override
    public FragmentEnum getFragmentEnum() {
        return FragmentEnum.SLIDING_FRAGMENT;
    }
}
