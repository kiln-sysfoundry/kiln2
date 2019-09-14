package org.sysfoundry.kiln.sql.subsys;


import com.google.common.collect.ImmutableMap;
import org.sysfoundry.kiln.sql.SQLServerConfig;
import org.sysfoundry.kiln.sys.LifecycleException;

import java.util.Map;

public class SQLServerImplTests {

    public void testSQLServerConfiguration() throws LifecycleException {
        SQLServerConfig config = getTestSQLServerConfig();
        SQLServerImpl sqlServer = new SQLServerImpl(config);
        sqlServer.start();
    }

    private SQLServerConfig getTestSQLServerConfig() {
        SQLServerConfig config = new SQLServerConfig();
        //Map<String,Object> datasources = new ImmutableMap.Builder<String,Object>()
        //        .put("default",new ImmutableMap.Builder<String,String>())
        //config.setDatasources();
        return config;
    }
}
