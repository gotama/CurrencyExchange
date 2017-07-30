package com.gautamastudios.currencyexchange.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gautamastudios.currencyexchange.R;
import com.gautamastudios.currencyexchange.enums.FragmentEnum;

public class LandingFragment extends BaseFragment {

    public static LandingFragment newInstance() {
        return new LandingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_landing, container, false);
        Button btnExchange = (Button) view.findViewById(R.id.btn_exchange);
        btnExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivityInterface.changeFragment(ExchangeFragment.newInstance());
            }
        });

        return view;
    }

    @Override
    public FragmentEnum getFragmentEnum() {
        return FragmentEnum.LANDING_FRAGMENT;
    }
}
