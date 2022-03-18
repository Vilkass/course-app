import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
 
public class Main extends Application {
	
    public static void main(String[] args) throws Exception {
    	launch(args);
    	 
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException {
    	
    	Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
		
    	Scene scene = new Scene(root);
    	scene.getStylesheets().add(getClass().getResource("/view/app.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
