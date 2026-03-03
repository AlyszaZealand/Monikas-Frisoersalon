module org.example.monikasfrisoersalon {


//    requires javafx.controls;
//    requires javafx.fxml;
//    requires java.sql;
//    requires java.desktop;
//
//    // Opens your packages so JavaFX can access them via reflection
//    opens org.example.monikasfrisoersalon to javafx.graphics, javafx.fxml;
//    opens org.example.monikasfrisoersalon.View.Controller to javafx.fxml;
//    opens org.example.monikasfrisoersalon.Model to javafx.base;


    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens org.example.monikasfrisoersalon to javafx.fxml;
    exports org.example.monikasfrisoersalon;
    exports org.example.monikasfrisoersalon.View;
    opens org.example.monikasfrisoersalon.View to javafx.fxml;
}