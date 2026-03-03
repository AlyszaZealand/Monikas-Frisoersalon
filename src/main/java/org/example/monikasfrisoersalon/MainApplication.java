package org.example.monikasfrisoersalon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.monikasfrisoersalon.View.Controller.Initialize;
import org.example.monikasfrisoersalon.View.Controller.LoginController;

public class MainApplication extends Application {

        @Override
        public void start(Stage stage) throws Exception {
            // 1. Build all services once
            Initialize init = new Initialize();

            // 2. Load FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource( "/login-view.fxml"));
            var url = getClass().getResource("/org/example/monikasfrisoersalon/login-view.fxml");
            System.out.println("FXML found at: " + url); // null = wrong path

            // 3. Inject services BEFORE load() is called
            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == LoginController.class) {
                    return new LoginController(init.getAuthenticatorService());
                }
                return null;
            });

            // 4. Load and show
            Scene scene = new Scene(loader.load());
            stage.setTitle("Monikas Frisørsalon");
            stage.setScene(scene);
            stage.show();
        }














//    @Override
//    public void start(Stage stage) throws Exception {
//        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/login-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//
//        stage.setTitle("Kalender");
//        stage.setScene(scene);
//        stage.show();
//    }
}
