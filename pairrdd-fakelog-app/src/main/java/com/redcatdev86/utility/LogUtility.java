package com.redcatdev86.utility;

import org.apache.log4j.Level;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LogUtility {

    private static final String DATE_FORMAT_PATTERN = "dd/MM/yyyy";

    private static final String COLON_PATTERN = ":";

    public static List<String> generateFakeLog() throws ParseException {
        List<String> logs = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        Date startDate = sdf.parse("01/10/2023");
        Date endDate = sdf.parse("31/10/2023");
        int randomNum;
        StringBuilder log;
        sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:s");
        for (int i = 0; i < 500000; i++) {
            Date rangeDate = rangeDate(startDate, endDate);
            randomNum = ThreadLocalRandom.current().nextInt(0, 2 + 1);
            log = new StringBuilder();
            switch (randomNum){
                case 0:
                    log.append(Level.ERROR);
                    break;
                case 1:
                    log.append(Level.INFO);
                    break;
                case 2:
                    log.append(Level.DEBUG);
                    break;
                default:
                    log.append(Level.WARN);
            }
            log.append(COLON_PATTERN);
            log.append(sdf.format(rangeDate));
            logs.add(log.toString());
        }
        return logs;
    }

    public static String[] splitLog(String logData){
        return logData.split(COLON_PATTERN);
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
