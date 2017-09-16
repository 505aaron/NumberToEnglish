package com.lessrework.text;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link NumberToWordFormat}.
 */
public class NumberToWordFormatTest {
    private NumberToWordFormat formatter = new NumberToWordFormat();

    @BeforeEach
    void setUp() {
    }

    @DisplayName("parseObject is not supported")
    @Test
    void parseObject() {
        assertThrows(UnsupportedOperationException.class, () -> formatter.parseObject(null, null));
    }

    @ParameterizedTest
    @MethodSource("upToTwentiesProvider")
    void testFormatValuesUpToTwenty(long valueToFormat, String expected) {
        StringBuffer buffer = new StringBuffer();
        assertEquals(expected, formatter.format(valueToFormat, buffer, null).toString());
    }

    @ParameterizedTest
    @MethodSource("twentiesToAHundredProvider")
    void twentiesToAHundred(long valueToFormat, String expected) {
        StringBuffer buffer = new StringBuffer();
        assertEquals(expected, formatter.format(valueToFormat, buffer, null).toString());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/sampled-values.csv")
    void sampledSource(long valueToFormat, String expected) {
        StringBuffer buffer = new StringBuffer();
        assertEquals(expected, formatter.format(valueToFormat, buffer, null).toString());
    }

    static Stream<Arguments> upToTwentiesProvider() {
        return Stream.of(
            Arguments.of(0L, "Zero"),
            Arguments.of(1L, "One"),
            Arguments.of(2L, "Two"),
            Arguments.of(3L, "Three"),
            Arguments.of(4L, "Four"),
            Arguments.of(5L, "Five"),
            Arguments.of(6L, "Six"),
            Arguments.of(7L, "Seven"),
            Arguments.of(8L, "Eight"),
            Arguments.of(9L, "Nine"),
            Arguments.of(10L, "Ten"),
            Arguments.of(11L, "Eleven"),
            Arguments.of(12L, "Twelve"),
            Arguments.of(13L, "Thirteen"),
            Arguments.of(14L, "Fourteen"),
            Arguments.of(15L, "Fifteen"),
            Arguments.of(16L, "Sixteen"),
            Arguments.of(17L, "Seventeen"),
            Arguments.of(18L, "Eighteen"),
            Arguments.of(19L, "Nineteen"),
            Arguments.of(20L, "Twenty")
        );
    }

    static Stream<Arguments> twentiesToAHundredProvider() {
        return Stream.of(
            Arguments.of(29L, "Twenty nine"),
            Arguments.of(38L, "Thirty eight"),
            Arguments.of(47L, "Forty seven"),
            Arguments.of(56L, "Fifty six"),
            Arguments.of(65L, "Sixty five"),
            Arguments.of(74L, "Seventy four"),
            Arguments.of(83L, "Eighty three"),
            Arguments.of(92L, "Ninety two"),
            Arguments.of(99L, "Ninety nine"),
            Arguments.of(100L, "One hundred")
        );
    }
}