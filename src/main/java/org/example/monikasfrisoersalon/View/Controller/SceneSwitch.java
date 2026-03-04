package org.example.monikasfrisoersalon.View.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.monikasfrisoersalon.Model.User;

import java.io.IOException;


public class SceneSwitch {

    private static Initialize container;

    public static void setContainer(Initialize initContainer) {
        container = initContainer;
    }

    public static void switchScene(ActionEvent event, String fxmlFile, User currentUser, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneSwitch.class.getResource(fxmlFile));
        // Her fortæller vi FXMLLoader, hvordan den skal oprette controllerne for de forskellige scener. Vi tjekker hvilken controller der skal bruges, og så opretter vi den med de services, den har brug for.
        loader.setControllerFactory(controllerClass -> {

            if (controllerClass == AdminController.class) {
                return new AdminController(
                        currentUser,
                        container.getEmployeeService(),
                        container.getAppointmentService()
                );
            }

            if (controllerClass == EmployeeController.class) {
                return new EmployeeController(
                        currentUser,
                        container.getAppointmentService(),
                        container.getCalendarService()
                );
            }

            return null;
        });

        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
