package org.sysfoundry.kiln.factory;

import lombok.Value;

@Value
class LookupRequest {

    private InstanceFactory requestingFactory;
    private Class requestedType;
}
