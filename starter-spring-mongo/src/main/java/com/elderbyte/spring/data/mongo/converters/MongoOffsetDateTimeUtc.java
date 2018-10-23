package com.elderbyte.spring.data.mongo.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * A converter which stores offset date times in UTC.
 */
public class MongoOffsetDateTimeUtc {

    @ReadingConverter
    public static class UtcToOffsetDateTimeConverter implements Converter<Date, OffsetDateTime> {
        @Override
        public OffsetDateTime convert(Date utcDate) {
            Instant instant = utcDate.toInstant();
            return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
        }
    }

    @WritingConverter
    public static class OffsetDateTimeToUtcConverter implements Converter<OffsetDateTime, Date> {
        @Override
        public Date convert(OffsetDateTime source) {
            var timepointUtc = source.toInstant();
            return Date.from(timepointUtc);
        }
    }

}
