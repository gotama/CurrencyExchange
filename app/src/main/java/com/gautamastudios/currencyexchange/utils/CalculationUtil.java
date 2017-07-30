package com.gautamastudios.currencyexchange.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class CalculationUtil {
    private static BigDecimal ONE_HUNDRED = new BigDecimal(100);
    private static BigDecimal ONE_HUNDRED_THOUSAND = new BigDecimal(100000);

    public static int getIntFromStringValue(String value) {
        if (!TextUtils.isEmpty(value)) {
            return Integer.valueOf(stripAllNonNumeric(value));
        } else {
            return 0;
        }
    }

    public static String stripAllNonNumeric(String value) {
        if (!TextUtils.isEmpty(value)) {
            return value.replaceAll("[^\\d]", "");
        }
        return "";
    }

    public static int convertDoubleValueToCents(double amount) {
        BigDecimal doubleValue = new BigDecimal(amount);
        return doubleValue.multiply(ONE_HUNDRED_THOUSAND).intValue();

    }

    public static String convertCentsToDecimalWithSymbol(int value) {
        NumberFormat n = NumberFormat.getCurrencyInstance();
        return n.format(value / 100.0);
    }

    public static BigDecimal multipleIntValueByRateInt(int amount, int rate) {
        BigDecimal rateValue = new BigDecimal(rate);
        BigDecimal bigRate = rateValue.divide(ONE_HUNDRED_THOUSAND).setScale(4, BigDecimal.ROUND_HALF_UP);

        BigDecimal value = new BigDecimal(amount).divide(ONE_HUNDRED).setScale(2, BigDecimal.ROUND_HALF_UP);

        return value.multiply(bigRate).setScale(4, BigDecimal.ROUND_HALF_UP);
    }
}
