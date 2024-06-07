module gui.chattingapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    requires org.hibernate.orm.core;
    requires java.sql;
    requires java.naming;
    requires java.mail;
    requires com.google.gson;
    requires jbcrypt;


    opens DTO to org.hibernate.orm.core,com.google.gson;


    opens gui to javafx.fxml;
    exports gui;
    exports Controller;
    exports DTO to com.google.gson;
    opens Controller to javafx.fxml;
}