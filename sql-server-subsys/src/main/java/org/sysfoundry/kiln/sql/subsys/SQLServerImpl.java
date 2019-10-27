package org.sysfoundry.kiln.sql.subsys;

import com.google.common.collect.ImmutableMap;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.slf4j.Logger;
import org.sysfoundry.kiln.health.Log;
import org.sysfoundry.kiln.sql.NoSuchDatasourceDefinedException;
import org.sysfoundry.kiln.sql.NoSuchJdbiInstanceDefinedException;
import org.sysfoundry.kiln.sql.SQLServer;
import org.sysfoundry.kiln.sql.SQLServerConfig;
import org.sysfoundry.kiln.sys.LifecycleException;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

import static org.sysfoundry.kiln.sql.subsys.SQLServerSubsys.NAME;

class SQLServerImpl implements SQLServer {

    private static final Logger log = Log.get(NAME);

    private SQLServerConfig slqServerConfig;

    private Map<String,DataSource> dataSourceMap;

    private Map<String,Jdbi> jdbiMap;

    public SQLServerImpl(SQLServerConfig sqlServerConfig) {
        this.slqServerConfig = sqlServerConfig;
    }

    @Override
    public void start() throws LifecycleException {
        if(slqServerConfig == null || slqServerConfig.getDatasources() == null){
            log.info("SQL Server Subsystem has not been configured. So the subsystem is not starting...");
        }else {
            log.info("Starting SQL Server with config {}...", slqServerConfig);

            createDatasources(slqServerConfig.getDatasources());

            createJdbiInstances(dataSourceMap);

            log.debug("Created dstasources {}",dataSourceMap);
        }
    }

    private void createJdbiInstances(Map<String, DataSource> dataSourceMap) {
        if(dataSourceMap != null && !dataSourceMap.isEmpty()){
            ImmutableMap.Builder<String, Jdbi> jdbiMapBuilder = new ImmutableMap.Builder<>();

            dataSourceMap.forEach((name,ds)->{
                Jdbi jdbi = Jdbi.create(ds);
                //register the sql object plugin
                jdbi.installPlugin(new SqlObjectPlugin());
                jdbiMapBuilder.put(name,jdbi);
            });

            jdbiMap = jdbiMapBuilder.build();
        }
    }

    private void createDatasources(Map<String, Object> datasources) {

        ImmutableMap.Builder<String, DataSource> dataSourceMapBuilder = new ImmutableMap.Builder<>();

        datasources.forEach((dsname,dspropmap)->{
            Properties dsproperties = mapToProperties((Map)dspropmap);
            HikariConfig hikariConfig = new HikariConfig(dsproperties);
            try {
                HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
                dataSourceMapBuilder.put(dsname, hikariDataSource);
            }catch(Exception e){
                log.warn(String.format("Failed to create datasource %s due to exception",dsname),e);
            }
        });

        dataSourceMap = dataSourceMapBuilder.build();
    }

    private Properties mapToProperties(Map propMap) {
        Properties newProperties = new Properties();
        propMap.forEach((k,v)->{
            newProperties.put(k,v);
        });
        return newProperties;
    }

    @Override
    public void stop() throws LifecycleException {
        log.info("Stopping SQL Server...");

        log.info("Clearing jdbi & datasource instances...");

        if(jdbiMap != null){
            jdbiMap = null;
        }

        if(dataSourceMap != null){
            dataSourceMap = null;
        }

    }

    @Override
    public DataSource getDatasource(String name) throws NoSuchDatasourceDefinedException {
        if(dataSourceMap != null && !dataSourceMap.isEmpty()){
            if(dataSourceMap.containsKey(name)){
                return dataSourceMap.get(name);
            }
        }
        throw new NoSuchDatasourceDefinedException(String.format("No datasource with key %s found",name));
    }

    @Override
    public Map<String, DataSource> getDatasourceMap() {
        return dataSourceMap;
    }

    @Override
    public Jdbi getJdbi(String name) throws NoSuchJdbiInstanceDefinedException {
        if(jdbiMap != null && !jdbiMap.isEmpty()){
            if(jdbiMap.containsKey(name)){
                return jdbiMap.get(name);
            }
        }
        throw new NoSuchJdbiInstanceDefinedException(String.format("No jdbi instance with key %s found",name));
    }

    @Override
    public Map<String, Jdbi> getJdbiMap() {
        return jdbiMap;
    }
}
