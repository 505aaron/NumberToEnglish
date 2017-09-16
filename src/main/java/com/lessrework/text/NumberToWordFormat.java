package com.lessrework.text;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>NumberToWordFormat allows formatting of whole numbers to their English word equivalent.</p>
 *
 * <p>
 * <b>Example</b>
 * </p>
 * <pre>
 * {@code
 *  NumberToWordFormat formatter = new NumberToWordFormat();
 *
 *  StringBuffer buffer = new StringBuffer();
 *  buffer = format.format(201, buffer, null);
 *  System.out.println(buffer.toString());
 *  // Outputs Two hundred and one.
 * }
 * </pre>
 */
public class NumberToWordFormat extends Format {
    private static final Map<Integer, String> hundredsFormatted;
    private static final Map<Integer, String> powersOfTenFormatted;

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

    /**
     * Formats whole number types according to english words.
     *
     * @param valueToFormat The value to format.
     * @param toAppendTo The {@link StringBuffer} to which the formatted text is to be appended.
     * @param pos {@link FieldPosition} is currently unused.
     * @return The value in as toAppendTo.
     * @throws NullPointerException if toAppendTo is null.
     */
    @Override
    public StringBuffer format(Object valueToFormat, StringBuffer toAppendTo, FieldPosition pos) {
        if (toAppendTo == null) {
            throw new NullPointerException("toAppendTo cannot be null");
        }

        if (valueToFormat instanceof Number) {
            return formatLong(((Number) valueToFormat).longValue(), toAppendTo);
        }

        return toAppendTo;
    }

    /**
     * Parses the string value.
     * <b>This operation is not supported</b>
     *
     * @param source The source to parse.
     * @param pos The parse position.
     * @return The parsed object.
     * @throws UnsupportedOperationException This operation is not supported.
     */
    @Override
    public Object parseObject(String source, ParsePosition pos) {
        throw new UnsupportedOperationException("Parsing is currently unsupported");
    }

    private StringBuffer formatLong(final long formatValue, final StringBuffer bufferToAppendTo) {
        if (formatValue < 0) {
            throw new UnsupportedOperationException("Only whole numbers are supported");
        }

        boolean firstEntry = true;

        if (formatValue == 0) {
            return bufferToAppendTo.append(capitalizeWord(formatHundredsValue(0L), true));
        }

        long currentValue = formatValue;
        int powersOfTen = (int)Math.log10(currentValue);
        Separator nextSeparator = Separator.NONE;

        while (powersOfTen >= 2) {
            int exponent = powersOfTen;
            int hundredsPart = powersOfTen % 3;
            if (hundredsPart > 0) {
                exponent = powersOfTen - hundredsPart;
            }

            long factorOfTen = (long) Math.pow(10.0, (double) exponent);
            long currentHundreds = currentValue / factorOfTen;

            bufferToAppendTo.append(nextSeparator);
            nextSeparator = Separator.NONE;
            formatHundredsPart(bufferToAppendTo, currentHundreds, firstEntry);
            firstEntry = false;

            currentValue -= currentHundreds * factorOfTen;
            powersOfTen = (int)Math.log10(currentValue);

            if (exponent > 1) {
                bufferToAppendTo.append(Separator.WORD);
                bufferToAppendTo.append(formatBaseTen(exponent));
                nextSeparator = powersOfTen < 2 ? Separator.AND : Separator.WORD;
            }
        }

        if (currentValue > 0) {
            bufferToAppendTo.append(nextSeparator);
            return formatHundredsPart(bufferToAppendTo, currentValue, firstEntry);
        }

        return bufferToAppendTo;
    }

    private StringBuffer formatHundredsPart(final StringBuffer buffer, final long value, final boolean firstEntry) {
            long hundredsPart = 0;
            Separator nextSeparator = Separator.NONE;

            boolean capitalize = firstEntry;

            if (value >= 100) {
                hundredsPart = value / 100;
                buffer.append(capitalizeWord(formatHundredsValue(hundredsPart), capitalize));
                buffer.append(Separator.HUNDRED);

                capitalize = false;
                nextSeparator = Separator.AND;
            }
            
            long teenValue = value - hundredsPart * 100;
            if (teenValue > 20) {
                long teenPart = (teenValue / 10) * 10; // Maps correctly for thirty, forty, etc.

                buffer.append(nextSeparator);

                buffer.append(capitalizeWord(formatHundredsValue(teenPart), capitalize));
                nextSeparator = Separator.WORD;

                capitalize = false;

                long digitValue = teenValue - teenPart;
                if (digitValue != 0) {
                    buffer.append(nextSeparator);
                    buffer.append(capitalizeWord(formatHundredsValue(digitValue), capitalize));
                }
            } else if (teenValue > 0){
                buffer.append(nextSeparator);

                buffer.append(capitalizeWord(formatHundredsValue(teenValue), capitalize));
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

    private enum Separator {
        NONE(""),
        WORD(" "),
        AND(" and "),
        HUNDRED(" hundred");

        private final String separatorText;

        private Separator(String separatorText) {
            this.separatorText = separatorText;
        }

        @Override
        public String toString() {
            return this.separatorText;
        }
    }
}
