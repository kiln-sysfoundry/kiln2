package org.sysfoundry.kiln.eventing.kafka.subsys;

import lombok.NonNull;
import org.sysfoundry.kiln.eventing.EventHandlerRegistration;
import org.sysfoundry.kiln.factory.InstanceFactory;
import org.sysfoundry.kiln.types.BaseTypedContainerFilter;

import java.util.Set;

public class EventHandlerRegistrationFilter extends BaseTypedContainerFilter {
    public EventHandlerRegistrationFilter(@NonNull InstanceFactory factory) {
        super(factory, EventHandlerRegistration.class, Set.class);
    }
}
