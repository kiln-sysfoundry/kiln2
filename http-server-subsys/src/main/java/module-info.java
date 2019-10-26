module org.sysfoundry.kiln.http.subsys {

    requires undertow.core;
    requires lombok;
    requires org.slf4j;
    requires org.sysfoundry.kiln.factory;
    requires org.sysfoundry.kiln.http;
    requires org.sysfoundry.kiln.sys;
    requires jjwt.api;
    requires org.sysfoundry.kiln.security;
    requires com.google.common;

    exports org.sysfoundry.kiln.http.subsys;
}