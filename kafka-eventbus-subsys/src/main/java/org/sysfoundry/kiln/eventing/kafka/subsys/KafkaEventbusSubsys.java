package org.sysfoundry.kiln.eventing.kafka.subsys;

import org.slf4j.Logger;
import org.sysfoundry.kiln.eventing.EventHandler;
import org.sysfoundry.kiln.eventing.EventHandlerRegistration;
import org.sysfoundry.kiln.eventing.Eventbus;
import org.sysfoundry.kiln.factory.FactoryException;
import org.sysfoundry.kiln.health.Log;
import org.sysfoundry.kiln.sys.BaseSubsys;
import org.sysfoundry.kiln.sys.LifecycleException;

import java.util.Optional;
import java.util.Set;

import static org.sysfoundry.kiln.factory.Util.flattenSets;

public class KafkaEventbusSubsys extends BaseSubsys<KafkaEventbusSubsysConfig> {

    public static final String NAME = "kafka-eventbus-subsys";

    private static final Logger log = Log.get(NAME);

    public KafkaEventbusSubsys(){
        super(KafkaEventbusInstanceFactory.class,KafkaEventbusSubsysConfig.class,NAME);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void start() throws LifecycleException {
        log.info("Starting {}...",NAME);

        try {
            Eventbus eventbus = lookup(Eventbus.class);

            log.info("Retrieved Kafka eventbus {}",eventbus);

            registerEventHandlers(eventbus);

            eventbus.start();
        } catch (FactoryException e) {
            throw new LifecycleException(e);
        }
    }

    private void registerEventHandlers(Eventbus eventbus) throws FactoryException {

        EventHandlerRegistrationFilter eventHandlerRegistrationFilter = lookup(EventHandlerRegistrationFilter.class);

        Optional<Set<Set<EventHandlerRegistration>>> optionalSetOfEventhanderRegistrationSet = eventHandlerRegistrationFilter.getAll(Set.class);


        log.debug("Event Handler RegistrationSetOptional isPresent {}",optionalSetOfEventhanderRegistrationSet.isPresent());

        Set<EventHandlerRegistration> setOfEventHandlerRegistrations = flattenSets(optionalSetOfEventhanderRegistrationSet,EventHandlerRegistration.class);


        log.debug("Set of EventHandler Registrations {}",setOfEventHandlerRegistrations.size());
        log.debug("Values {}",setOfEventHandlerRegistrations);
        for (EventHandlerRegistration eventHandlerRegistration : setOfEventHandlerRegistrations) {
            log.debug("Registering {}",eventHandlerRegistration);
            String[] topics = eventHandlerRegistration.getTopics();
            EventHandler eventHandler = eventHandlerRegistration.getEventHandler();

            for (String topic : topics) {
                eventbus.subscribe(topic,eventHandler);
            }

        }


    }

    @Override
    public void stop() throws LifecycleException {
        log.info("Stopping {}...",NAME);

        try {
            Eventbus eventbus = lookup(Eventbus.class);

            log.info("Retrieved Kafka eventbus {}",eventbus);

            eventbus.stop();
        } catch (FactoryException e) {
            throw new LifecycleException(e);
        }

    }
}
