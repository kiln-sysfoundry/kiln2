package org.sysfoundry.kiln.sys;

import org.sysfoundry.kiln.factory.InstanceFactory;
import org.sysfoundry.kiln.factory.MapBaseInstanceFactory;
import org.sysfoundry.kiln.health.Status;

import static org.sysfoundry.kiln.factory.Util.imMap;
import static org.sysfoundry.kiln.factory.Util.singleton;

public class BaseSubsysInstanceFactory extends MapBaseInstanceFactory {
    public BaseSubsysInstanceFactory(InstanceFactory parent) {
        super(parent, imMap(
                singleton(parent, Status.class,instanceFactory -> {
                    return new Status(Status.State.OK);
                })
        ));
    }
}
