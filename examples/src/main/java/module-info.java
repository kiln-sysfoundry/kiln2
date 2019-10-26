module org.sysfoundry.kiln.examples {

    requires org.sysfoundry.kiln.sys;
    requires org.sysfoundry.kiln.factory;
    requires typesafe.config;
    requires org.sysfoundry.kiln.http.subsys;
    requires org.sysfoundry.kiln.sql.subsys;
    requires lombok;
    requires com.fasterxml.jackson.core;
    requires org.sysfoundry.kiln.http;
    requires org.sysfoundry.kiln.formats.data;

    exports org.sysfoundry.kiln.example1;
    exports org.sysfoundry.kiln.example2;
    exports org.sysfoundry.kiln.model.account;
}