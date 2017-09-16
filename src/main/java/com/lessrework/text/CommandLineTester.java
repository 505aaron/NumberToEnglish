package com.lessrework.text;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import java.io.PrintStream;
import java.time.Duration;
import java.time.Instant;

public class CommandLineTester {
    @Parameter(names = {"--value", "-v"}, description = "The value to format.", required = true)
    private Long value = null;

    @Parameter(names = {"--measure", "-m"}, description = "Measures format time")
    private boolean measure = false;

    public static void main(String[] args) {
        CommandLineTester options = new CommandLineTester();

        try {
            JCommander.newBuilder()
                    .addObject(options)
                    .build()
                    .parse(args);
        } catch (ParameterException e) {
            e.usage();
            return;
        }

        Instant start = Instant.now();
        NumberToWordFormat format = new NumberToWordFormat();
        StringBuffer buffer = new StringBuffer();
        format.format(options.value, buffer, null);
        System.out.printf("%,d='%s'%n", options.value, buffer.toString());

        Instant end = Instant.now();

        if (options.measure) {
            Duration duration = Duration.between(start, end);
            System.out.printf("Duration %sms%n", duration.toMillis());
        }
    }
}
