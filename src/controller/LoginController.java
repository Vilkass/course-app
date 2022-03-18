package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Administrator;
import model.CourseIS;
import model.Student;
import utils.DatabaseConnection;

public class LoginController implements Initializable{

	@FXML private Button loginBtn;
	@FXML private Button registerBtn;
	@FXML private TextField loginUser;
	@FXML private PasswordField loginPass;
	@FXML private TextField regName;
	@FXML private PasswordField regPass;
	@FXML private PasswordField regConfirmPass;
	@FXML private RadioButton loginStudentCheck;
	@FXML private RadioButton regStudentCheck;
	@FXML private RadioButton passAdminCheck;
	@FXML private RadioButton regAdminCheck;
	
	
	private Student student;
	private Administrator admin;
	private CourseIS courseIS;
	
	private Connection con;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
		try {
			con = DatabaseConnection.connect();
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error!");
			alert.setHeaderText(null);
			alert.setContentText("Failed to coonect to database");
			alert.showAndWait();
		}
		
		
	
	}
	
	
	public void logIn(ActionEvent event) throws IOException {
		
		if(loginUser != null && loginPass != null) {
				
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MainWindow.fxml"));
			Parent root = loader.load();
			MainWindow mainWindow = loader.getController();
			
			Stage stage = null;
			
			try {
				if(loginStudentCheck.isSelected()) {
					// Student logging in
				
					String query = "SELECT LOGIN, PASS, ID FROM students WHERE LOGIN = \"" + loginUser.getText() + "\" AND PASS = \"" + loginPass.getText() + "\"";
					Statement st;
			
					st = con.createStatement();
					ResultSet result = st.executeQuery(query);
					
					if(result.next()) {
						mainWindow.setStudId(Integer.parseInt(result.getString(3)));
						stage = (Stage)loginUser.getScene().getWindow();
					}else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error!");
						alert.setHeaderText(null);
						alert.setContentText("Wrong ussername or password!");
						alert.showAndWait();
						return;
					}
					
					
	
				}else {
					// Admin logging in
				
					String query = "SELECT LOGIN, PASS, ID FROM admins WHERE LOGIN = \"" + loginUser.getText() + "\" AND PASS = \"" + loginPass.getText() + "\"";
					Statement st;
			
					st = con.createStatement();
					ResultSet result = st.executeQuery(query);
					
					if(result.next()) {
						mainWindow.setAdminId(Integer.parseInt(result.getString(3)));
						stage = (Stage)loginUser.getScene().getWindow();
					}else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error!");
						alert.setHeaderText(null);
						alert.setContentText("Wrong ussername or password!");
						alert.showAndWait();
						return;
					}

				
				}

			} catch (SQLException e) {
				
				e.printStackTrace();
				return;
			}
			
			
			mainWindow.setConnection(con);
			mainWindow.initialize(null, null);
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/view/app.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
	
		}
		
		
	
	}
	
	
	public void register(ActionEvent event) {
		if(regPass.getText().equals("") || regName.getText().equals("")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error!");
			alert.setHeaderText(null);
			alert.setContentText("Please fill information");
			alert.showAndWait();
			return;
		}
		
		if(regPass.getText().equals(regConfirmPass.getText())) {
			
			try {
				
				if(regStudentCheck.isSelected()) {
					// Create student
					PreparedStatement statement = con.prepareStatement("INSERT INTO students VALUES(?,?,?,?,?,?,?,?)");
					statement.setInt(1, 0);
			    	statement.setString(2, regName.getText());
			    	statement.setString(3, regPass.getText());
			    	statement.setString(4, null);
			    	statement.setString(5, null);
			    	statement.setString(6, null);
			    	statement.setString(7, null);
			    	statement.setString(8, null);
			    	int res2 = statement.executeUpdate();
			    	
			    	if(res2 > 0) {
			    		Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Success!");
						alert.setHeaderText(null);
						alert.setContentText("User successfully created!");
						alert.showAndWait();
						regPass.setText("");
						regName.setText("");
						regConfirmPass.setText("");
			    	}else {
			    		Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error!");
						alert.setHeaderText(null);
						alert.setContentText("Failed to create user!");
						alert.showAndWait();
			    	}
					
					
				}else {
					// Create admin
					PreparedStatement statement = con.prepareStatement("INSERT INTO admins VALUES(?,?,?,?,?,?,?,?)");
					statement.setInt(1, 0);
			    	statement.setString(2, regName.getText());
			    	statement.setString(3, regPass.getText());
			    	statement.setString(4, null);
			    	statement.setString(5, null);
			    	statement.setString(6, null);
			    	statement.setString(7, null);
			    	statement.setString(8, null);
			    	int res2 = statement.executeUpdate();
			    	
			    	if(res2 > 0) {
			    		Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Success!");
						alert.setHeaderText(null);
						alert.setContentText("User successfully created!");
						alert.showAndWait();
						regPass.setText("");
						regName.setText("");
						regConfirmPass.setText("");
			    	}else {
			    		Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error!");
						alert.setHeaderText(null);
						alert.setContentText("Failed to create user!");
						alert.showAndWait();
			    	}
				}

			}catch(Exception e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error!");
				alert.setHeaderText(null);
				alert.setContentText("Username is taken!");
				alert.showAndWait();
				e.printStackTrace();
				return;
			}
			
		}else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error!");
			alert.setHeaderText(null);
			alert.setContentText("Password do not match!");
			alert.showAndWait();
		}
		
		
		
		
	}


	
}
