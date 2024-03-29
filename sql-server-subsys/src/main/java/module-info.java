module org.sysfoundry.kiln.sql.subsys {

    requires lombok;
    requires org.sysfoundry.kiln.factory;
    requires org.sysfoundry.kiln.sql;
    requires org.slf4j;
    requires org.sysfoundry.kiln.sys;
    requires com.zaxxer.hikari;
    requires java.sql;
    requires org.jdbi.v3.core;
    requires org.jdbi.v3.sqlobject;
    requires com.google.common;

    exports org.sysfoundry.kiln.sql.subsys;
}