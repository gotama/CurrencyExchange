package com.gautamastudios.currencyexchange.events;

public class CalculateAmountStarted {

    String mAmountToCalculate = "";

    public CalculateAmountStarted(String amountToCalculate) {
        this.mAmountToCalculate = amountToCalculate;
    }

    public String getAmountToCalculate() {
        return mAmountToCalculate;
    }
}
