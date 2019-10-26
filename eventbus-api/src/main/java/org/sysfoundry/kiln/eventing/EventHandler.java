package org.sysfoundry.kiln.eventing;

import java.util.UUID;

public interface EventHandler {

    public UUID getID();

    public void onEvent(String topic,Event event);
}
