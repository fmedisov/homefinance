package ru.medisov.home_finance.common.utils;

import java.math.BigDecimal;

public class MoneyUtils {

    public static BigDecimal inBigDecimal(Double doubleValue) {
        return MoneyUtils.getBaseAmount().add(BigDecimal.valueOf(doubleValue));
    }

    public static BigDecimal inBigDecimal(Long longValue) {
        return MoneyUtils.getBaseAmount().add(BigDecimal.valueOf(longValue));
    }

    public static BigDecimal inBigDecimal(Float floatValue) {
        return MoneyUtils.getBaseAmount().add(BigDecimal.valueOf(floatValue));
    }

    public static BigDecimal inBigDecimal(Integer intValue) {
        return MoneyUtils.getBaseAmount().add(BigDecimal.valueOf(intValue));
    }

    public static BigDecimal inBigDecimal(String stringValue) {
        Double doubleValue = Double.parseDouble(stringValue);
        return inBigDecimal(doubleValue);
    }

    public static BigDecimal getBaseAmount() {
        return BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_CEILING);
    }
}
