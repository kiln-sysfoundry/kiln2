package org.sysfoundry.kiln.example2;

import lombok.NonNull;
import org.sysfoundry.kiln.factory.InstanceFactory;
import org.sysfoundry.kiln.sys.BaseSubsys;
import org.sysfoundry.kiln.sys.LifecycleException;

public class Example2Subsys extends BaseSubsys<Example2SubsysConfig> {
    public static final String NAME = "example2-subsys";

    public Example2Subsys() {
        super(Example2SubsysFactory.class, Example2SubsysConfig.class, NAME);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void start() throws LifecycleException {

    }

    @Override
    public void stop() throws LifecycleException {

    }
}
