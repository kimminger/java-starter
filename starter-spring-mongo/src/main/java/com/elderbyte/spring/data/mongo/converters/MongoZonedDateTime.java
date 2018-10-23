package com.elderbyte.spring.data.mongo.converters;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.time.*;
import java.util.Date;

/**
 * A loss free representation of a zoned date time in mongo db.
 * I.e. we store the date in UTC (Zulu Time) but also remember the original timezone / offset.
 *
 * Therefore a date is stored as embedded document similar to:
 *
 * date: {
 *     utc: "2018-10-19T05:50:13Z"
 *     offset: "7200"
 *     zone: "Europe/Zurich"
 * }
 *
 *
 * This class is immutable.
 */
public class MongoZonedDateTime {

    @ReadingConverter
    public static class DocumentToZonedDateTimeConverter implements Converter<Document, ZonedDateTime> {
        @Override
        public ZonedDateTime convert(Document source) {
            return MongoZonedDateTime.from(source).asZonedDateTime();
        }
    }

    @WritingConverter
    public static class ZonedDateTimeToDocumentConverter implements Converter<ZonedDateTime, Document> {
        @Override
        public Document convert(ZonedDateTime source) {
            return MongoZonedDateTime.from(source).toDbObject();
        }
    }

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/


    public static MongoZonedDateTime from(Document dbObject){
        var zone = (String)dbObject.get("zone");
        var offset = (int)dbObject.get("offset");
        var utcDate = (Date)dbObject.get("utc");
        Instant instant = utcDate.toInstant();
        var utc = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        return new MongoZonedDateTime(utc, ZoneOffset.ofTotalSeconds(offset), ZoneId.of(zone));
    }

    public static MongoZonedDateTime now(){
        return from(ZonedDateTime.now());
    }

    /**
     * Returns a mongo zoned date time from the given zoned date time.
     */
    public static MongoZonedDateTime from(ZonedDateTime zonedDateTime){

        var utc = zonedDateTime
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();

        return new MongoZonedDateTime(
                utc,
                zonedDateTime.getOffset(),
                zonedDateTime.getZone()
        );
    }

    /***************************************************************************
     *                                                                         *
     * Fields                                                                  *
     *                                                                         *
     **************************************************************************/

    /**
     * The date time as instant in UTC
     */
    private LocalDateTime utc;

    /**
     * The offset in original time
     */
    private int offset;

    /**
     * The time zone in original time
     */
    private String zone;

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    /**
     * Internal Constructor
     */
    protected MongoZonedDateTime() { }

    /**
     * Creates a new MongoZonedDateTime
     * @param utc The UTC date time
     * @param offset The offset
     * @param zoneId THe zone id
     */
    public MongoZonedDateTime(LocalDateTime utc, ZoneOffset offset, ZoneId zoneId){
        this.utc = utc;
        this.offset = offset.getTotalSeconds();
        this.zone = zoneId.getId();
    }

    /***************************************************************************
     *                                                                         *
     * Public property                                                         *
     *                                                                         *
     **************************************************************************/


    public LocalDateTime getUtc() {
        return utc;
    }

    public int getOffset() {
        return offset;
    }

    public String getZone() {
        return zone;
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    public Document toDbObject(){
        var date = new Document();
        var instant = utc.toInstant(ZoneOffset.UTC);
        var utcDate = Date.from(instant);

        date.append("utc", utcDate);
        date.append("offset", offset);
        date.append("zone", zone);
        return date;
    }

    /**
     * Assembles the original zoned date time from [utc, offset, zone].
     */
    public ZonedDateTime asZonedDateTime(){

        var offset = ZoneOffset.ofTotalSeconds(this.offset);
        var zoneId = ZoneId.of(zone);

        return ZonedDateTime.ofLocal(
                utc.plusSeconds(offset.getTotalSeconds()),
                zoneId,
                offset
        );
    }

}