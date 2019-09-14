package org.sysfoundry.kiln.sql;

import lombok.Data;

@Data
public class DatasourceConfig {

    private String datasourceClassName;
    private String userName;
    private String password;

}
