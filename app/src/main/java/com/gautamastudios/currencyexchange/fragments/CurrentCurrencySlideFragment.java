package com.gautamastudios.currencyexchange.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.gautamastudios.currencyexchange.R;
import com.gautamastudios.currencyexchange.enums.FragmentEnum;
import com.gautamastudios.currencyexchange.events.CalculateAmountStarted;
import com.gautamastudios.currencyexchange.events.ViewPagerPageChange;
import com.gautamastudios.currencyexchange.models.SupportedCurrenciesModel;
import com.gautamastudios.currencyexchange.utils.AmountsToDisplayUtil;
import com.gautamastudios.currencyexchange.utils.CalculationUtil;

public class CurrentCurrencySlideFragment extends BaseFragment {
    private static final String SUPPORTED_CURRENCY = "supported_currency";
    private static final String DEFAULT_AMOUNT = "0.00";
    private SupportedCurrenciesModel mSupportedCurrency;
    private View mView;
    private EditText mEditText;
    private String mAmountToCalculate;

    public void register() {
        if (!mEventBus.isRegistered(this)) {
            mEventBus.register(this);
        }
    }

    public void unregister() {
        this.mEventBus.unregister(this);
    }

    public void onEventMainThread(ViewPagerPageChange e) {
        drawUI();
        if (e.isClearEditText()) {
            mEditText.setText(DEFAULT_AMOUNT);
            mEventBus.post(new CalculateAmountStarted(mAmountToCalculate));
        }
    }

    public static CurrentCurrencySlideFragment newInstance(SupportedCurrenciesModel supportedCurrenciesModel) {
        CurrentCurrencySlideFragment fragment = new CurrentCurrencySlideFragment();
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


    private void drawUI() {
        ((TextView) mView.findViewById(R.id.currency_name_text_view)).setText(mSupportedCurrency.getCurrencyCode());
        ((TextView) mView.findViewById(R.id.customer_amount_text_view)).setText(getString(R.string.default_own_amount));

        mEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_current_currency_slide, container, false);

            mEditText = (EditText) mView.findViewById(R.id.amount_input_edit_text);
            InputFilter[] filters = new InputFilter[1];
            filters[0] = new InputFilter.LengthFilter(11);
            mEditText.setFilters(filters);
            mEditText.setText(AmountsToDisplayUtil.displayAmountWithCurrency(DEFAULT_AMOUNT, mSupportedCurrency.getCurrencyCode(), true));
            mEditText.addTextChangedListener(new TextWatcher() {

                boolean mEditing = false;

                @Override
                public synchronized void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public synchronized void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public synchronized void afterTextChanged(Editable s) {
                    if (!mEditing) {
                        mEditing = true;
                        int currentSelectionIndex = mEditText.getSelectionStart();

                        String cleanString = CalculationUtil.stripAllNonNumeric(s.toString());
                        mEditText.setText(AmountsToDisplayUtil.displayAmountWithCurrency(cleanString, mSupportedCurrency.getCurrencyCode(), true));
                        mAmountToCalculate = CalculationUtil.stripAllNonNumeric(mEditText.getText().toString());
                        if (!mAmountToCalculate.equals("000")) {
                            mEventBus.post(new CalculateAmountStarted(mAmountToCalculate));
                        }


                        if (currentSelectionIndex == s.length() || Integer.valueOf(cleanString) == 0) {
                            mEditText.setSelection(mEditText.getText().length());
                        } else {
                            mEditText.setSelection(currentSelectionIndex);
                        }
                        mEditing = false;
                    }
                }
            });


        }
        drawUI();
        return mView;
    }

    @Override
    public FragmentEnum getFragmentEnum() {
        return FragmentEnum.SLIDING_FRAGMENT;
    }
}
