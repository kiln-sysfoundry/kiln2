module org.sysfoundry.kiln.sys {

    requires lombok;
    requires org.slf4j;
    requires typesafe.config;
    requires org.sysfoundry.kiln.factory;


    exports org.sysfoundry.kiln.sys;
    exports org.sysfoundry.kiln.health;
    exports org.sysfoundry.kiln;
}