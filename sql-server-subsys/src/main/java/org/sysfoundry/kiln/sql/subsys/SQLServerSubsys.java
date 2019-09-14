package org.sysfoundry.kiln.sql.subsys;

import org.slf4j.Logger;
import org.sysfoundry.kiln.factory.FactoryException;
import org.sysfoundry.kiln.health.Log;
import org.sysfoundry.kiln.sql.SQLServer;
import org.sysfoundry.kiln.sql.SQLServerConfig;
import org.sysfoundry.kiln.sys.BaseSubsys;
import org.sysfoundry.kiln.sys.LifecycleException;

public class SQLServerSubsys extends BaseSubsys<SQLServerConfig> {

    public static final String NAME = "sql-server-subsys";

    private static final Logger log = Log.get(NAME);

    public SQLServerSubsys(){
        super(SQLServerInstanceFactory.class, SQLServerConfig.class, NAME);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void start() throws LifecycleException {
        try {
            SQLServer sqlServer = lookup(SQLServer.class);
            sqlServer.start();
        } catch (FactoryException e) {
            throw new LifecycleException(e);
        }
    }

    @Override
    public void stop() throws LifecycleException {
        try {
            SQLServer sqlServer = lookup(SQLServer.class);
            sqlServer.stop();
        } catch (FactoryException e) {
            throw new LifecycleException(e);
        }
    }
}
