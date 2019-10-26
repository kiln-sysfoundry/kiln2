package org.sysfoundry.kiln.eventing;

import lombok.Value;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Value
public class Event {

    private UUID uuid;
    private Instant generatedTime;
    private String source;
    private Map<String,String> headers;
    private String message;


    public static Event newEvent(String src,Map<String,String> headers,String messageBody){
        return new Event(UUID.randomUUID(),Instant.now(),src,headers,messageBody);
    }
}
