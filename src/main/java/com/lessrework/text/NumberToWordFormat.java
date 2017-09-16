package com.lessrework.text;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by acordova05 on 9/15/17.
 */
public class NumberToWordFormat extends Format {
    private static final Map<Long, String> hundredsFormatted = new HashMap();
    private static final Map<Long, String> powersOfTenFormatted = new HashMap();
    public static final String WORD_SEPARATOR = " ";
    public static final String HUNDRED_SEPARATOR = " and ";

    static {
        hundredsFormatted.put(0L, "zero");
        hundredsFormatted.put(1L, "one");
        hundredsFormatted.put(2L, "two");
        hundredsFormatted.put(3L, "three");
        hundredsFormatted.put(4L, "four");
        hundredsFormatted.put(5L, "five");
        hundredsFormatted.put(6L, "six");
        hundredsFormatted.put(7L, "seven");
        hundredsFormatted.put(8L, "eight");
        hundredsFormatted.put(9L, "nine");
        hundredsFormatted.put(10L, "ten");
        hundredsFormatted.put(11L, "eleven");
        hundredsFormatted.put(12L, "twelve");
        hundredsFormatted.put(13L, "thirteen");
        hundredsFormatted.put(14L, "fourteen");
        hundredsFormatted.put(15L, "fifteen");
        hundredsFormatted.put(16L, "sixteen");
        hundredsFormatted.put(17L, "seventeen");
        hundredsFormatted.put(18L, "eighteen");
        hundredsFormatted.put(19L, "nineteen");
        hundredsFormatted.put(20L, "twenty");
        hundredsFormatted.put(30L, "thirty");
        hundredsFormatted.put(40L, "forty");
        hundredsFormatted.put(50L, "fifty");
        hundredsFormatted.put(60L, "sixty");
        hundredsFormatted.put(70L, "seventy");
        hundredsFormatted.put(80L, "eighty");
        hundredsFormatted.put(90L, "ninety");

        powersOfTenFormatted.put(3L, "thousand");
        powersOfTenFormatted.put(6L, "million");
        powersOfTenFormatted.put(9L, "billion");
        powersOfTenFormatted.put(12L, "trillion");
        powersOfTenFormatted.put(15L, "quadrillion");
        powersOfTenFormatted.put(18L, "quintillion");
    }

    @Override
    public StringBuffer format(Object valueToFormat, StringBuffer toAppendTo, FieldPosition pos) {
        if (valueToFormat instanceof Number) {
            return formatLong(((Number) valueToFormat).longValue(), toAppendTo);
        }

        return toAppendTo;
    }

    /**
     * Parses the string value.
     * <b>This operation is not supported</b>
     * 
     * @throws UnsupportedOperationException This operation is not supported.
     * @param source The source to parse.
     * @param pos The parse position.
     * @return The parsed object.
     */
    @Override
    public Object parseObject(String source, ParsePosition pos) {
        throw new UnsupportedOperationException("Parsing is currently unsupported");
    }

    private StringBuffer formatLong(final long formatValue, final StringBuffer bufferToAppendTo) {
        if (formatValue < 0) {
            throw new UnsupportedOperationException("Number is not part of the whole number set.");
        }

        boolean firstEntry = true;

        if (formatValue == 0) {
            return bufferToAppendTo.append(capitalizeWord(hundredsFormatted.get(0L), true));
        }

        long currentValue = formatValue;
        int powersOfTen = (int)Math.log10(currentValue);

        while (powersOfTen >= 2) {
            int exponent = powersOfTen;
            int hundredsPart = powersOfTen % 3;

            if (hundredsPart > 0) {
                exponent = powersOfTen - hundredsPart;
            }

            long factorOfTen = (long) Math.pow(10.0, (double) exponent);
            long currentHundreds = currentValue / factorOfTen;

            formatHundredsPart(bufferToAppendTo, currentHundreds, firstEntry);
            firstEntry = false;

            if (exponent > 1) {
                bufferToAppendTo.append(WORD_SEPARATOR);
                bufferToAppendTo.append(formatBaseTen(exponent));
                bufferToAppendTo.append(WORD_SEPARATOR);
            }

            currentValue -= currentHundreds * factorOfTen;
            powersOfTen = (int)Math.log10(currentValue);
        }

        if (currentValue > 0) {
            return formatHundredsPart(bufferToAppendTo, currentValue, firstEntry);
        }

        return bufferToAppendTo;
    }

    private StringBuffer formatHundredsPart(final StringBuffer buffer, final long value, boolean firstEntry) {

            long hundredsPart = 0;
            boolean appendAnd = false;
            if (value >= 100) {
                hundredsPart = value / 100;
                buffer.append(capitalizeWord(hundredsFormatted.get(hundredsPart), firstEntry));
                buffer.append(" hundred");


                firstEntry = false;
                appendAnd = true;
            }
            
            long teenValue = value - hundredsPart * 100;
            if (teenValue > 20) {
                long teenPart = (teenValue / 10) * 10; // Maps correctly for thirty, forty, etc.

                if (appendAnd) {
                    buffer.append(HUNDRED_SEPARATOR);
                }

                buffer.append(capitalizeWord(hundredsFormatted.get(teenPart), firstEntry));
                buffer.append(WORD_SEPARATOR);

                firstEntry = false;

                long digitValue = teenValue - teenPart;
                if (digitValue != 0) {
                    buffer.append(capitalizeWord(hundredsFormatted.get(digitValue), firstEntry));
                }
            } else if (teenValue > 0){
                if (appendAnd) {
                    buffer.append(HUNDRED_SEPARATOR);
                }

                buffer.append(capitalizeWord(hundredsFormatted.get(teenValue), firstEntry));
            }

            return buffer;
    }

    private String formatBaseTen(long baseTen) {
        return powersOfTenFormatted.getOrDefault(baseTen, "ERROR");
    }

    private String capitalizeWord(String value, boolean shouldCapitalize) {
        if (shouldCapitalize) {
            return value.substring(0, 1).toUpperCase() + value.substring(1);
        }

        return value;
    }
}
