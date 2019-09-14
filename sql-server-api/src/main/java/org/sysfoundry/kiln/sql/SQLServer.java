package org.sysfoundry.kiln.sql;

import org.jdbi.v3.core.Jdbi;
import org.sysfoundry.kiln.sys.LifecycleException;

import javax.sql.DataSource;
import java.util.Map;

public interface SQLServer {

    public void start()throws LifecycleException;

    public void stop()throws LifecycleException;

    public DataSource getDatasource(String name) throws NoSuchDatasourceDefinedException;

    public Map<String,DataSource> getDatasourceMap();


    public Jdbi getJdbi(String name) throws NoSuchJdbiInstanceDefinedException;

    public Map<String,Jdbi> getJdbiMap();
}
