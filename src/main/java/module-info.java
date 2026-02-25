module org.example.monikasfrisoersalon {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.monikasfrisoersalon to javafx.fxml;
    exports org.example.monikasfrisoersalon;
}