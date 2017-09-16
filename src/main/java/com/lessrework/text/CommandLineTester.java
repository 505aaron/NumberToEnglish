package com.lessrework.text;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.time.Duration;
import java.time.Instant;

public class CommandLineTester {

    @Parameter(names = {"--value", "-v"}, description = "The value to format.", required = true)
    private long value;

    @Parameter(names = {"--measure", "-m"}, description = "Measures format time")
    private boolean measure;

    public static void main(String[] args) {
        CommandLineTester options = new CommandLineTester();
        JCommander.newBuilder()
            .addObject(options)
            .build()
            .parse(args);

        Instant start = Instant.now();
        NumberToWordFormat format = new NumberToWordFormat();
        StringBuffer buffer = new StringBuffer();
        format.format(options.value, buffer, null);
        System.out.printf("%d,'%s'%n", options.value, buffer.toString());

        Instant end = Instant.now();

        if (options.measure) {
            Duration duration = Duration.between(start, end);
            System.out.printf("Duration %sms%n", duration.toMillis());
        }
    }
}
