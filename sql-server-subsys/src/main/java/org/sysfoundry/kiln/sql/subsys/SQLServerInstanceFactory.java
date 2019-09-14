package org.sysfoundry.kiln.sql.subsys;

import org.sysfoundry.kiln.factory.FactoryException;
import org.sysfoundry.kiln.factory.InstanceFactory;
import org.sysfoundry.kiln.factory.MapBaseInstanceFactory;
import org.sysfoundry.kiln.sql.SQLServer;
import org.sysfoundry.kiln.sql.SQLServerConfig;

import static org.sysfoundry.kiln.factory.Util.imMap;
import static org.sysfoundry.kiln.factory.Util.singleton;

public class SQLServerInstanceFactory extends MapBaseInstanceFactory {
    public SQLServerInstanceFactory(InstanceFactory parent) {
        super(parent, imMap(
            singleton(parent, SQLServer.class, instanceFactory -> {
                SQLServerConfig sqlServerConfig = null;
                try {
                    sqlServerConfig = instanceFactory.get(SQLServerConfig.class);
                } catch (FactoryException e) {
                    //throw new ConstructionFailedException(e);
                }

                return new SQLServerImpl(sqlServerConfig);
            })
        ));
    }
}
