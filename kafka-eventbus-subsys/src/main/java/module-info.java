module org.sysfoundry.kiln.eventing.kafka.subsys {

    requires org.slf4j;
    requires org.sysfoundry.kiln.sys;
    requires kafka.clients;
    requires lombok;
    requires org.sysfoundry.kiln.eventing;
    requires org.sysfoundry.kiln.factory;
    requires com.fasterxml.jackson.core;
    requires org.sysfoundry.kiln.formats.data;
    requires com.google.common;

    exports org.sysfoundry.kiln.eventing.kafka.subsys;
}