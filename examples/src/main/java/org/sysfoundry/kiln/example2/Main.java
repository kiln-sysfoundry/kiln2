package org.sysfoundry.kiln.example2;

import com.typesafe.config.ConfigFactory;
import org.sysfoundry.kiln.http.subsys.HttpSubsys;
import org.sysfoundry.kiln.sql.subsys.SQLServerSubsys;
import org.sysfoundry.kiln.sys.LifecycleException;
import org.sysfoundry.kiln.sys.Sys;

import java.time.Duration;
import java.time.Instant;

public class Main {

    public static void main(String[] args) {
        Instant start = Instant.now();

        Sys sys = new Sys(ConfigFactory.defaultApplication()
                ,Example2Subsys.class
                ,SQLServerSubsys.class
                ,HttpSubsys.class
        );

        try {
            sys.start();
            Instant end = Instant.now();

            System.out.println(String.format("Started in %d ms", Duration.between(start,end).toMillis()));
            //sys.stop();

        } catch (LifecycleException e) {
            e.printStackTrace();
        }

    }
}
