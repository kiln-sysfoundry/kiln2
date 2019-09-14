package org.sysfoundry.kiln.http;

import lombok.NonNull;
import org.sysfoundry.kiln.factory.InstanceFactory;
import org.sysfoundry.kiln.types.BaseTypedContainerFilter;

import java.util.Set;

public class HttpHandlerRouteFilter extends BaseTypedContainerFilter {

    public HttpHandlerRouteFilter(@NonNull InstanceFactory factory) {
        super(factory, HttpHandlerRoute.class, Set.class);
    }
}
