module org.sysfoundry.kiln.security {

    requires lombok;
    requires org.slf4j;
    requires jjwt.api;

    exports org.sysfoundry.kiln.security.idm;
    exports org.sysfoundry.kiln.security.jwt;

}