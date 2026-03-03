module org.example.monikasfrisoersalon {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens org.example.monikasfrisoersalon to javafx.fxml;
    exports org.example.monikasfrisoersalon;
    exports org.example.monikasfrisoersalon.View;
    opens org.example.monikasfrisoersalon.View to javafx.fxml;
}