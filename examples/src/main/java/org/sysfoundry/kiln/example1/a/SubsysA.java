package org.sysfoundry.kiln.example1.a;

import org.slf4j.Logger;
import org.sysfoundry.kiln.factory.FactoryException;
import org.sysfoundry.kiln.health.Log;
import org.sysfoundry.kiln.sys.BaseSubsys;
import org.sysfoundry.kiln.sys.LifecycleException;

public class SubsysA extends BaseSubsys<SubsysAConfig> {

    private static final String NAME = "subsys-a";

    private static final Logger log = Log.get(NAME);

    public SubsysA() {
        super(SubsysAFactory.class, SubsysAConfig.class,NAME);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void start() throws LifecycleException {
        log.info("Starting...");

        try {
            A a = lookup(A.class);
            log.info("Doning stuff with A {}",a);
        } catch (FactoryException e) {
            throw new LifecycleException(e);
        }

    }

    @Override
    public void stop() throws LifecycleException {
        log.info("Stopping...");
    }
}
