package org.sysfoundry.kiln.eventing;

public interface Eventbus {

    public void publish(String topic,Event event);

    public void subscribe(String topic,EventHandler handler);

    public void start();

    public void stop();
}
