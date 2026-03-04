package org.example.monikasfrisoersalon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.monikasfrisoersalon.View.Controller.Initialize;
import org.example.monikasfrisoersalon.View.Controller.LoginController;
import org.example.monikasfrisoersalon.View.Controller.SceneSwitch;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Her opretter vi en instans af Initialize, som er vores "container" for alle services. Vi sætter den også i SceneSwitch, så vi kan bruge den til at oprette controllers senere.
        Initialize init = new Initialize();
        SceneSwitch.setContainer(init);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/monikasfrisoersalon/login-view.fxml"));
        loader.setControllerFactory(controllerClass -> {
            if (controllerClass == LoginController.class) {
                return new LoginController(init.getAuthenticatorService());
            }
            return null;
        });

        Scene scene = new Scene(loader.load());
        stage.setTitle("Monikas Frisørsalon - Login");
        stage.setScene(scene);
        stage.show();
    }
}


