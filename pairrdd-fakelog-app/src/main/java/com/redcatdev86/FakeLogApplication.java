package com.redcatdev86;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Hello world!
 *
 */
public class FakeLogApplication {

    private static final String DATE_FORMAT_PATTERN = "dd/MM/yyyy";
    public static void main( String[] args ) throws ParseException{
        List<String> logs = generateFakeLog();
        logs.forEach(log -> System.out.println(log));
    }

    private static List<String> generateFakeLog() throws ParseException {
        List<String> logs = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        Date startDate = sdf.parse("01/10/2023");
        Date endDate = sdf.parse("31/10/2023");
        for (int i = 0; i<500000;i++){
            Date rangeDate = rangeDate(startDate, endDate);
            logs.add(sdf.format(rangeDate));
        }
        return logs;
    }

    private static Date rangeDate(Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom
                .current()
                .nextLong(startMillis, endMillis);

        return new Date(randomMillisSinceEpoch);
    }
}
