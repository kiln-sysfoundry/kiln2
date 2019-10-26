package org.sysfoundry.kiln.eventing;

import lombok.Value;

@Value
public class EventHandlerRegistration {

    private String[] topics;
    private EventHandler eventHandler;

}
