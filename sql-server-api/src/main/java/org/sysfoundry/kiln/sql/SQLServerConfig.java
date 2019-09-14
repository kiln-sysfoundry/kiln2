package org.sysfoundry.kiln.sql;

import lombok.Data;

import java.util.Map;
import java.util.Properties;

@Data
public class SQLServerConfig {

    private Map<String, Object> datasources;

}
