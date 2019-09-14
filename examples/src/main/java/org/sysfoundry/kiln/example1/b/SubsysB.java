package org.sysfoundry.kiln.example1.b;

import org.slf4j.Logger;
import org.sysfoundry.kiln.example1.a.A;
import org.sysfoundry.kiln.factory.FactoryException;
import org.sysfoundry.kiln.health.Log;
import org.sysfoundry.kiln.sys.BaseSubsys;
import org.sysfoundry.kiln.sys.LifecycleException;

public class SubsysB extends BaseSubsys<SubsysBConfig> {

    private static final String NAME = "subsys-b";

    private static final Logger log = Log.get(NAME);

    public SubsysB() {
        super(SubsysBFactory.class,SubsysBConfig.class,NAME);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void start() throws LifecycleException {
        log.info("Starting..");
        try {
            A a = lookup(A.class);
            log.info("Doning stuff with A {}",a);
            B b = lookup(B.class);
            log.info("Doning stuff with B {}",b);

        } catch (FactoryException e) {
            throw new LifecycleException(e);
        }


    }

    @Override
    public void stop() throws LifecycleException {
        log.info("Stopping...");
    }
}
