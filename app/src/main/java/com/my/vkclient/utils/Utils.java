package com.my.vkclient.utils;

import com.my.vkclient.Constants;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Utils {
    private static Utils instance;
    private SimpleDateFormat simpleDateFormat;

    private Utils() {
        simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault());
    }

    public static Utils getInstance() {
        if (instance == null) {
            instance = new Utils();
        }

        return instance;
    }

    public String getSimpleDate(long unixTime) {
        return simpleDateFormat.format(new java.util.Date(unixTime * 1000));
    }

    public String formatNumber(int number) {
        if (number == 0) {
            return Constants.STRING_EMPTY;
        }

        if (number < Constants.INT_THOUSAND) {
            return String.valueOf(number);
        }

        int exp = (int) (Math.log(number) / Math.log(Constants.INT_THOUSAND));

        return String.format(Locale.US, Constants.STRING_NUMBER_FORMAT, number / Math.pow(Constants.INT_THOUSAND, exp), Constants.STRING_NUMBER_POSTFIX.charAt(exp - 1));
    }
}
