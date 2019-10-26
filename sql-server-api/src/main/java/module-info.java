module org.sysfoundry.kiln.sql {

    requires lombok;
    requires org.slf4j;
    requires org.jdbi.v3.core;
    requires org.sysfoundry.kiln.sys;
    requires java.sql;

    exports org.sysfoundry.kiln.sql;
}