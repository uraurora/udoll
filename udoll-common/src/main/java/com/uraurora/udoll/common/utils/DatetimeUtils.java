package com.uraurora.udoll.common.utils;

import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author : gaoxiaodong04
 * @program : udoll
 * @date : 2022-02-09 17:15
 * @description :
 */
public abstract class DatetimeUtils {

    private DatetimeUtils() {
    }

    /**
     * LocalDateTimeFormatter
     */
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");

    /**
     * DateFormatter
     */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    /**
     * 统一东八区
     */
    public static final ZoneId ZONE_ID = ZoneId.of("+8");

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZONE_ID).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZONE_ID).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZONE_ID).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZONE_ID).toLocalDateTime();
    }

    public static LocalDateTime asLocalDateTime(FileTime fileTime) {
        return fileTime.toInstant().atZone(ZONE_ID).toLocalDateTime();
    }

    public static String nowDateTime(){
        return DATE_TIME_FORMATTER.format(LocalDateTime.now(ZONE_ID));
    }

    public static String nowDate(){
        return DATE_FORMATTER.format(LocalDateTime.now(ZONE_ID));
    }

}
