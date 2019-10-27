module org.sysfoundry.kiln.formats.data {

    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires lombok;
    requires transitive com.fasterxml.jackson.module.paramnames;
    requires transitive com.fasterxml.jackson.datatype.jdk8;
    requires transitive com.fasterxml.jackson.datatype.jsr310;

    exports org.sysfoundry.kiln.formats.data;

}