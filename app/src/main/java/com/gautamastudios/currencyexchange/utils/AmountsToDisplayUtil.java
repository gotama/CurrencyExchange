package com.gautamastudios.currencyexchange.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class AmountsToDisplayUtil {
    private static BigDecimal ONE_HUNDRED = new BigDecimal(100);
    private static BigDecimal ONE_HUNDRED_THOUSAND = new BigDecimal(100000);
    private final static String DECIMAL_FORMAT = "###,###,##0.0000";
    private final static String SIMPLE_DECIMAL_FORMAT = "###,###,##0.00";

    public static String displayBigDecimalWithCurrency(BigDecimal bigDecimal, String currencyCode) {
        BigDecimal bigDecimalamount = bigDecimal;
        return getFormattedAmount(bigDecimalamount, currencyCode, true);
    }

    public static String displayAmountWithCurrency(String value, String currencyCode, boolean isSimpleDecimal) {
        BigDecimal bigDecimalamount;
        if (isSimpleDecimal) {
            bigDecimalamount = new BigDecimal(correctDecimalSeparators(value)).divide(ONE_HUNDRED);
        } else {
            bigDecimalamount = new BigDecimal(correctDecimalSeparators(value)).divide(ONE_HUNDRED_THOUSAND);
        }

        return getFormattedAmount(bigDecimalamount, currencyCode, isSimpleDecimal);
    }

    private static String correctDecimalSeparators(String amount) {

        amount = amount.replaceAll("[\\.,:]", ".");
        String[] parts = amount.split("\\.");
        StringBuilder sb = new StringBuilder();

        if (parts.length > 1) {
            for (int i = 0; i < parts.length - 1; i++) {
                sb.append(parts[i]);
            }
            sb.append(".").append(parts[parts.length - 1]);
        } else if (parts.length == 0) {
            sb.append("0");
        } else {
            sb.append(parts[0]).append(".00");
        }

        return sb.toString();
    }

    private static String getFormattedAmount(BigDecimal amount, String currencyCode, boolean isSimpleDecimal) {
        switch (currencyCode) {
            case "USD":
                currencyCode = "$";
                break;
            case "GBP":
                currencyCode = "£";
                break;
            case "EUR":
                currencyCode = "€";
                break;
            default:
                currencyCode = "";
                break;
        }

        String format = "";
        if (isSimpleDecimal) {
            format = new DecimalFormat(SIMPLE_DECIMAL_FORMAT, new DecimalFormatSymbols(Locale.UK)).format(amount.doubleValue());
        } else {
            format = new DecimalFormat(DECIMAL_FORMAT, new DecimalFormatSymbols(Locale.UK)).format(amount.doubleValue());
        }
        return String.format("%s %s", currencyCode, format);
    }
}
