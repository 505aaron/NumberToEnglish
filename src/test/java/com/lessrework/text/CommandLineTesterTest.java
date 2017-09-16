package com.lessrework.text;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sun.nio.cs.US_ASCII;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {#link {@link CommandLineTester}
 */
class CommandLineTesterTest {
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;

    private static final String USAGE_MESSAGE = "Usage: <main class> [options]\n" +
            "  Options:\n" +
            "    --measure, -m\n" +
            "      Measures format time\n" +
            "      Default: false\n" +
            "  * --value, -v\n" +
            "      The value to format.\n\n";

    @BeforeEach
    public void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();

        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }
    
    @Test
    void usage() throws IOException {
        CommandLineTester.main(new String[0]);

        assertEquals(USAGE_MESSAGE, outContent.toString());
        assertEquals("", errContent.toString());
    }

    @Test
    void valueFormat() {
        CommandLineTester.main(new String[] { "-v", "100"});

        assertEquals("100='One hundred'\n", outContent.toString());
    }

    @Test
    void valueFormatMeasure() throws IOException {
        CommandLineTester.main(new String[] { "-v", "100", "-m"});
        outContent.flush();
        
        assertEquals("100='One hundred'\n" +
                "Duration 0ms\n", outContent.toString());
        assertTrue("Contains Duration", outContent.toString().startsWith("100='One hundred'\nDuration"));
    }

    @Test
    void valueInvalidValue() throws IOException {
        CommandLineTester.main(new String[] { "-v", "0xA", "-m"});
        outContent.flush();

        assertEquals(USAGE_MESSAGE, outContent.toString());
    }
}