package com.gautamastudios.currencyexchange.enums;

import com.gautamastudios.currencyexchange.fragments.DisplayCurrencySlideFragment;
import com.gautamastudios.currencyexchange.fragments.ExchangeFragment;
import com.gautamastudios.currencyexchange.fragments.LandingFragment;

public enum FragmentEnum {
    LANDING_FRAGMENT(1, LandingFragment.class.getName()),
    EXCHANGE_FRAGMENT(2, ExchangeFragment.class.getName()),
    SLIDING_FRAGMENT(3, DisplayCurrencySlideFragment.class.getName());

    private int id;
    private String fragmentTag;

    FragmentEnum(int id, String fragmentTag) {
        this.id = id;
        this.fragmentTag = fragmentTag;
    }

    public int getId() {
        return id;
    }

    public String getFragmentTag() {
        return fragmentTag;
    }
}
