package org.sysfoundry.kiln.example1;

import com.typesafe.config.ConfigFactory;
import org.sysfoundry.kiln.example1.a.SubsysA;
import org.sysfoundry.kiln.example1.b.SubsysB;
import org.sysfoundry.kiln.sys.LifecycleException;
import org.sysfoundry.kiln.sys.Sys;

public class Main {

    public static void main(String[] args) throws LifecycleException {
        Sys sys = new Sys(
                ConfigFactory.defaultApplication(),
                SubsysA.class,
                SubsysB.class
        );
        sys.start();

        sys.stop();
    }
}
