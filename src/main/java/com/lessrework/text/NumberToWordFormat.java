package com.lessrework.text;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by acordova05 on 9/15/17.
 */
public class NumberToWordFormat extends Format {
    private static final Map<Integer, String> hundredsFormatted;
    private static final Map<Integer, String> powersOfTenFormatted;
    public static final String WORD_SEPARATOR = " ";
    public static final String HUNDRED_SEPARATOR = " and ";

    static {
        Map<Integer, String> hundreds = new HashMap<>();
        hundreds.put(0, "zero");
        hundreds.put(1, "one");
        hundreds.put(2, "two");
        hundreds.put(3, "three");
        hundreds.put(4, "four");
        hundreds.put(5, "five");
        hundreds.put(6, "six");
        hundreds.put(7, "seven");
        hundreds.put(8, "eight");
        hundreds.put(9, "nine");
        hundreds.put(10, "ten");
        hundreds.put(11, "eleven");
        hundreds.put(12, "twelve");
        hundreds.put(13, "thirteen");
        hundreds.put(14, "fourteen");
        hundreds.put(15, "fifteen");
        hundreds.put(16, "sixteen");
        hundreds.put(17, "seventeen");
        hundreds.put(18, "eighteen");
        hundreds.put(19, "nineteen");
        hundreds.put(20, "twenty");
        hundreds.put(30, "thirty");
        hundreds.put(40, "forty");
        hundreds.put(50, "fifty");
        hundreds.put(60, "sixty");
        hundreds.put(70, "seventy");
        hundreds.put(80, "eighty");
        hundreds.put(90, "ninety");
        hundredsFormatted = Collections.unmodifiableMap(hundreds);

        HashMap<Integer, String> powersOfTen = new HashMap<>();
        powersOfTen.put(3, "thousand");
        powersOfTen.put(6, "million");
        powersOfTen.put(9, "billion");
        powersOfTen.put(12, "trillion");
        powersOfTen.put(15, "quadrillion");
        powersOfTen.put(18, "quintillion");
        powersOfTenFormatted = Collections.unmodifiableMap(powersOfTen);
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
            return bufferToAppendTo.append(capitalizeWord(formatHundredsValue(0L), true));
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
                buffer.append(capitalizeWord(formatHundredsValue(hundredsPart), firstEntry));
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

                buffer.append(capitalizeWord(formatHundredsValue(teenPart), firstEntry));
                buffer.append(WORD_SEPARATOR);

                firstEntry = false;

                long digitValue = teenValue - teenPart;
                if (digitValue != 0) {
                    buffer.append(capitalizeWord(formatHundredsValue(digitValue), firstEntry));
                }
            } else if (teenValue > 0){
                if (appendAnd) {
                    buffer.append(HUNDRED_SEPARATOR);
                }

                buffer.append(capitalizeWord(formatHundredsValue(teenValue), firstEntry));
            }

            return buffer;
    }

    private String formatHundredsValue(long teenValue) {
        return hundredsFormatted.get((int)teenValue);
    }

    private String formatBaseTen(long baseTen) {
        return powersOfTenFormatted.getOrDefault((int)baseTen, "ERROR");
    }

    private String capitalizeWord(String value, boolean shouldCapitalize) {
        if (shouldCapitalize) {
            return value.substring(0, 1).toUpperCase() + value.substring(1);
        }

        return value;
    }
}
