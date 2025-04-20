package com.atm;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.util.Pair;

public class TimeFormatter
{
    public static Pair<String, String> getTime()
    {
        LocalDateTime now = LocalDateTime.now();

        // Formatting the current date into DD/M/Y

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        String formattedDate = now.format(dateFormatter);

        // Formatting the current time into xx:xxPM/AM
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mma");
        String formattedTime = now.format(timeFormatter);

        return new Pair<>(formattedDate, formattedTime);
    }
}
