/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.utils;

/**
 *
 * @author thomas
 */
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class DateUtils {

    public static final DateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    public static final DateFormat FULL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    public static final DateFormat UTC_ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    public static final SimpleDateFormat SDF_ISO_DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final SimpleDateFormat SDF_SIMPLE_DATE = new SimpleDateFormat("yyyyMMdd");

    public static final SimpleDateFormat SDF_ISO_DATE = new SimpleDateFormat("yyyy-MM-dd");

    public static final SimpleDateFormat SDF_FULL_ISO_DATETIME = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    public static final DateTimeFormatter DTF_TIME_FORMAT_HOURS_MINS = DateTimeFormatter.ofPattern("HH:mm", Locale.FRANCE);

    public static final DateTimeFormatter DTF_TIME_FORMAT_HOURS_MINS_SECS = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.FRANCE);

    public static final DateTimeFormatter DTF_TIME_FORMAT_HOURS_MINS_SECS_ZONE = DateTimeFormatter.ofPattern("HH:mm:ssX", Locale.FRANCE);

    public static final DateTimeFormatter DTF_FULL_ISO_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.FRANCE);

    public static final DateTimeFormatter DTF_ISO_DATETIME_ZONE = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");

    public static final DateTimeFormatter DTF_ISO_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static Date parseDate(String dateAsString) throws ParseException {
        return SIMPLE_DATE_FORMAT.parse(dateAsString);
    }

    public static String formatDate(Date date) {
        return SIMPLE_DATE_FORMAT.format(date);
    }

    public static Date returnMostSpecificDate(Map<SimpleDateFormat, String> dates) {
        SimpleDateFormat mostSpecificFormat = SDF_SIMPLE_DATE;//new ArrayList<SimpleDateFormat>(dates.keySet()).get(0);
        for (Map.Entry<SimpleDateFormat, String> entry : dates.entrySet()) {
            String dateString = entry.getValue();
            SimpleDateFormat format = entry.getKey();

            if (dateString != null && format.toPattern().length() > mostSpecificFormat.toPattern().length()) {
                mostSpecificFormat = format;
            }
        }
        Date date = null;
        String dateString = dates.get(mostSpecificFormat);
        if (dateString != null) {
            try {
                mostSpecificFormat.setLenient(false);
                date = mostSpecificFormat.parse(dateString);
            } catch (ParseException ex) {

            }
        }
        return date;
    }

    /**
     * *
     * Parse a String date against a whole set of date formats
     *
     * @param date
     * @param formats
     * @return
     */
    public static Date parse(String date, SimpleDateFormat[] formats) {
        Date result = null;
        if (date != null) {
            for (SimpleDateFormat format : formats) {
                format.setLenient(false);
                try {
                    result = format.parse(date);
                } catch (ParseException ex) {
                }
            }
        }
        return result;
    }

    /**
     * *
     * Convert a String time to a SQL Timestamp. The String can contain timezone
     * information (offsets or Z) and can contain the ISO8601 T separator.
     * Without timezone information, UTC is understood.
     *
     * @param time
     * @return
     */
    public static Timestamp stringToSQLTimestamp(String time) {
        return Timestamp.from(timeToInstant(time)); //e.g.  '2011-12-03T10:15:30+01:00'
    }

    /**
     * *
     * Convert a Date time to a SQL Timestamp. The Date time is understood to be
     * in UTC.
     *
     * @param time
     * @return
     */
    public static Timestamp dateToSQLTimestamp(Date time) {
        return Timestamp.from(time.toInstant());
    }

    public static Instant SQLTimestampToInstant(Timestamp time) {
        return time.toInstant();
    }

    /**
     * *
     * Convert a String time to a SQL Timestamp. The String can contain timezone
     * information. Without timezone information, UTC is understood.
     *
     * @param time
     * @return
     */
    public static Instant timeToInstant(String time) { // e.g. '2011-12-03T10:15:30Z'
        if (time == null) {
            return null;
        } else {
            Instant i;
            try {
                i = OffsetDateTime.parse(time, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toInstant(); //e.g.  '2011-12-03T10:15:30+01:00'
            } catch (DateTimeParseException e) {
                try {
                    i = OffsetDateTime.of(LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME), ZoneOffset.UTC).toInstant();//e.g. '2011-12-03T10:15:30' and UTC is understood
                } catch (DateTimeParseException e2) {
                    i = OffsetDateTime.of(LocalDateTime.parse(time, DateTimeFormatter.ISO_INSTANT), ZoneOffset.UTC).toInstant();//e.g. '2011-12-03T10:15:30Z for instance for UTC' 
                }
            }
            return i;
        }
    }

}
