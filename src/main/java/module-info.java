module org.example.monikasfrisoersalon {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.monikasfrisoersalon to javafx.fxml;
    exports org.example.monikasfrisoersalon;
}