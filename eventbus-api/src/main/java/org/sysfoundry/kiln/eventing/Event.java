package org.sysfoundry.kiln.eventing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

@Value
public class Event {

    private UUID uuid;
    private String generatedTime;
    private String source;
    private Map<String,String> headers;
    private Object message;


    public static Event newEvent(String src,Map<String,String> headers,Object messageBody){
        return new Event(UUID.randomUUID(),ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT),src,headers,messageBody);
    }

    public static Event newEvent(ZonedDateTime instant,String src,Object messageBody,Map<String,String> headers){
        return new Event(UUID.randomUUID(),instant.format(DateTimeFormatter.ISO_INSTANT),src,headers,messageBody);
    }

    @JsonCreator
    public static Event createEvent(@JsonProperty("uuid") UUID uuid,
                                    @JsonProperty("generatedTime") String generatedTime,
                                    @JsonProperty("source") String source,
                                    @JsonProperty("headers") Map<String,String> headers,
                                    @JsonProperty("message") Object message){
        return new Event(uuid,generatedTime,source,headers,message);
    }
}
