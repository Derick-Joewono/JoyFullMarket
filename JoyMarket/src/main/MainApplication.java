package main;

import javafx.application.Application;
import javafx.stage.Stage;
import view.RegisterPage;

public class MainApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        RegisterPage registerPage = new RegisterPage();
        registerPage.show(stage);
    }
}
