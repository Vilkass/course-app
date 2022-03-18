package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.Administrator;
import model.Course;
import model.CourseIS;
import model.File;
import model.Folder;
import model.Student;
import utils.DatabaseConnection;
import utils.EditMyCourses;
import utils.EnrolledCoursesUtil;

public class MainWindow implements Initializable{

	
	@FXML private TabPane tabPane;

	// Main window
	@FXML private ListView<String> allCoursesListView;
	@FXML private Button test;
	@FXML private Button mainEnrollButton;
	@FXML private Label mainAdmin;
	@FXML private Label mainStartDate;
	@FXML private Label mainEndDate;
	@FXML private Label mainPrice;
	@FXML private Label mainTotalStudents;
	@FXML private Label mainTotalFolders;
	@FXML private Label mainTotalFiles;
	
	// My courses
	@FXML private Tab courseCreation;
	@FXML private TextField myCourseName;
	@FXML private TextField myCourseSDate;
	@FXML private TextField myCourseEDate;
	@FXML private TextField myCoursePrice;
	@FXML private Button createCourseBtn;
	@FXML private Button addFolderFileBtn;
	@FXML private ChoiceBox<String> editCourseBox;
	@FXML private TreeView<String> myCoursesTreeView;
	
	
	// Enrolled courses
	@FXML private TreeView<String> enrolledCourseTreeView;
	@FXML private ChoiceBox<String> enrolledCourseChoiceBox;
	@FXML private Button leaveCourseButton;
	@FXML private Label enrolledCourseAdmin;
	@FXML private Label enrolledCourseStartDate;
	@FXML private Label enrolledCourseEndDate;
	@FXML private Label enrolledCoursePrice;
	@FXML private Tab enrolledCoursesTab;
	
	
	// Account information
	@FXML private Label accType;
	@FXML private Label name;
	@FXML private Label lastName;
	@FXML private Label email;
	@FXML private Label password;
	@FXML private Label creditCard;
	@FXML private Label totalEnrolled;
	@FXML private Label change1;
	@FXML private Label change2;
	@FXML private Label change3;
	@FXML private TextField textChange1;
	@FXML private TextField textChange2;
	@FXML private TextField textChange3;
	@FXML private ChoiceBox<String> changeChoice;
	@FXML private Button confirmChange;
	@FXML private AnchorPane informationPane;
	@FXML private Button logOutBtn;
	@FXML private PasswordField pass1;
	@FXML private PasswordField pass2;
	@FXML private PasswordField pass3;
	
	private ArrayList<String> choices = new ArrayList<>();
	private ArrayList<String> choices2 = new ArrayList<>();
	private Student student = new Student();
	//private CourseIS courseIS;
	private ArrayList<String> folders = new ArrayList<>();
	private ArrayList< ArrayList<String> > files = new ArrayList<ArrayList<String>>();
	private Administrator admin = new Administrator();
	private Connection con;

	public void setStudId(int id) {
		this.student.setId(id);
	}
	
	public void setAdminId(int id) {
		this.admin.setId(id);
	}
	public void setConnection(Connection con) {
		this.con = con;
	}
	
	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if(student.getId() == 0 && admin.getId() == 0) {
			return;
		}else {
			
			removeTabs();
			initChoiceBoxes();
			showAllAvailableCourses();
			displayCourseInfo();
		}
		
	}
	
	// Main window
	public void showAllAvailableCourses() {
			allCoursesListView.getItems().clear();
			String query = "SELECT NAME FROM course";
			try {
				Statement st = con.createStatement();
				ResultSet result = st.executeQuery(query);
				
				while(result.next()) {
					allCoursesListView.getItems().add(result.getString(1));
				}	
				
			}catch(Exception e) {
				
			}
			
	}
		
	public void enrollToCourse() {
		
		
		try {
			String query = "SELECT ID FROM course WHERE NAME = \"" + allCoursesListView.getSelectionModel().getSelectedItem() + "\"";
			Statement s1 = con.createStatement();
			ResultSet r1 = s1.executeQuery(query);
			String courseID = "";
			String query2 = "";
			if(r1.next()) {
				query2 = "SELECT COUNT(COURSE_ID) FROM enrolledCourses WHERE STUDENT_ID = " + student.getId() + " AND COURSE_ID = " + r1.getString(1);
				courseID = r1.getString(1);
			}
			ResultSet r2 = s1.executeQuery(query2);
			if(r2.next()) {
				if(Integer.parseInt(r2.getString(1)) == 0) {
					// Check if credit card verified
					
					query = "SELECT CREDIT_CARD_ID FROM students WHERE ID = ?";
					PreparedStatement st = con.prepareStatement(query);
					st.setInt(1, student.getId());
					r1 = st.executeQuery();
					if(r1.next()) {
						
						if(r1.getInt(1) != 0) {
							// Enroll to course
							String query3 = "INSERT INTO enrolledCourses VALUES(" + student.getId() + "," + courseID+")";
							int r3 = s1.executeUpdate(query3);
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Success!");
							alert.setHeaderText(null);
							alert.setContentText("You successfully enrolled to " +  allCoursesListView.getSelectionModel().getSelectedItem());
							alert.showAndWait();	
						}else {
							// Update credit card
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Error!");
							alert.setHeaderText(null);
							alert.setContentText("You need to add credit card to enroll to courses!");
							alert.showAndWait();
						}		
						
					}

					
					
					
					
					
					
				}else {
					// Already enrolled
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText(null);
					alert.setContentText("You already enrolled in this course");
					alert.showAndWait();
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
			
	}
	
	
	public void displayCourseInfo() {
		
		allCoursesListView.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				
				String query = "SELECT ID, START_DATE, END_DATE, PRICE, CREATOR_ID FROM course WHERE NAME = \"" +allCoursesListView.getSelectionModel().getSelectedItem()+"\"" ;
				try {
					Statement s1 = con.createStatement();
					ResultSet r1 = s1.executeQuery(query);
					int courseID = 0;
					if(r1.next()) {
						courseID = r1.getInt(1);
						String query2 = "SELECT NAME, LAST_NAME FROM admins WHERE ID = " + r1.getString(5);
						Statement s2 = con.createStatement();
						ResultSet r2 = s2.executeQuery(query2);
						if(r2.next()) {
							mainAdmin.setText("Admin: " + r2.getString(1) + " " + r2.getString(2));
						}
						mainStartDate.setText("Start date: " + r1.getString(2));
						mainEndDate.setText("End date: " + r1.getString(3));
						mainPrice.setText("Price: " + r1.getString(4));
						
						String query3 = "SELECT COUNT(COURSE_ID) FROM enrolledCourses WHERE COURSE_ID = " + r1.getString(1);
						ResultSet r3 = s1.executeQuery(query3);
						while(r3.next()) {
							mainTotalStudents.setText("Total enrolled students: " + r3.getString(1));
						}
						
						int totalFolders = 0;
						int totalFiles = 0;
						query3 = "SELECT FOLDER_ID FROM courseAndfolders WHERE COURSE_ID = ?";
						PreparedStatement st3 = con.prepareStatement(query3);
						st3.setInt(1, courseID);
						r3 = st3.executeQuery();
						
						while(r3.next()) {
							query3 = "SELECT COUNT(FOLDER_ID) FROM folderAndFiles WHERE FOLDER_ID = ?";
							PreparedStatement st4 = con.prepareStatement(query3);
							st4.setInt(1, r3.getInt(1));
							ResultSet rs4 = st4.executeQuery();
							if(rs4.next()) {
								totalFiles += rs4.getInt(1);
							}
	
							totalFolders++;
						}
						
						mainTotalFolders.setText("Total folders: " + totalFolders);
						mainTotalFiles.setText("Total files: " + totalFiles);

					}
					
				}catch(Exception e) {
					e.printStackTrace();
				}
				
				
				
			}
			
		});
		
		
		
	}
	
	
	
	// Main window ends
	
	
	
	
	// My courses
	
	public void createNewCourse(ActionEvent event) {
		
		if(!myCourseName.getText().equals("") && !myCourseSDate.getText().equals("") && !myCourseEDate.getText().equals("") && !myCoursePrice.getText().equals("")) {
			
			LocalDate startDate = null;
			LocalDate endDate = null;
			double price = 0.;
			try {
				startDate = LocalDate.parse(myCourseSDate.getText());
				endDate = LocalDate.parse(myCourseEDate.getText());
			}catch(Exception e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error!");
				alert.setHeaderText(null);
				alert.setContentText("Wrong date format!");
				alert.showAndWait();
				return;
			}
			try {
				price = Double.parseDouble(myCoursePrice.getText());
			}catch(Exception e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error!");
				alert.setHeaderText(null);
				alert.setContentText("Wrong price");
				alert.showAndWait();
				return;
			}
			
			try {
				String query = "INSERT INTO course VALUES(?,?,?,?,?,?)";
				PreparedStatement s1 = con.prepareStatement(query);
				s1.setString(1, "0");
				s1.setString(2, myCourseName.getText());
				s1.setString(3, startDate.toString());
				s1.setString(4, endDate.toString());
				s1.setString(5, String.valueOf(price));
				s1.setInt(6, admin.getId());
				
				int r1 = s1.executeUpdate();
				
				if(r1 != 0){
					// Successfully created
					
					
					String query2 = "SELECT ID FROM course ORDER BY ID DESC LIMIT 1";
					s1 = con.prepareStatement(query2);
					ResultSet r2 = s1.executeQuery();
					int courseID = 0;
					if(r2.next()) {
						courseID = r2.getInt(1);
					}
					
					
					String query3 = "INSERT INTO createdCourses VALUES(?,?)";
					s1 = con.prepareStatement(query3);
					s1.setInt(1, admin.getId());
					s1.setInt(2, courseID);
					int r3 = s1.executeUpdate();
					
					
					
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Success!");
					alert.setHeaderText(null);
					alert.setContentText("Course created successfully!");
					alert.showAndWait();
					
					myCourseName.setText("");
					myCourseSDate.setText("");
					myCourseEDate.setText("");
					myCoursePrice.setText("");
					showAllAvailableCourses();
					showCreatedCourses();
				}
				
				
			}catch(SQLIntegrityConstraintViolationException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error!");
				alert.setHeaderText(null);
				alert.setContentText("Course " + myCourseName.getText() + " already exists!");
				alert.showAndWait();
				
			}catch(Exception e1) {
				
				e1.printStackTrace();
			}
			
		}else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error!");
			alert.setHeaderText(null);
			alert.setContentText("Fill all fields!");
			alert.showAndWait();
		}
	
		
	}
	
	public void showCreatedCourses() {

		
		editCourseBox.getItems().clear();
		try {
			String query = "SELECT c.NAME FROM createdCourses as e, course as c WHERE e.ADMIN_ID = "+admin.getId()+" AND e.COURSE_ID = c.ID;";
			
			Statement s1 = con.createStatement();
			ResultSet r1 = s1.executeQuery(query);
			
			while(r1.next()) {
				editCourseBox.getItems().add(new String(r1.getString(1)));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void deleteSelectedCourse(ActionEvent event) {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm deletion!");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure want to delete " + editCourseBox.getSelectionModel().getSelectedItem()+"?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			
			try {
				
				String query = "SELECT ID FROM COURSE WHERE NAME = ?";
				PreparedStatement st = con.prepareStatement(query);
				st.setString(1, editCourseBox.getValue());
				ResultSet rs = st.executeQuery();
				int courseID = 0;
				if(rs.next()) {
					courseID = rs.getInt(1);
					

					query = "DELETE FROM createdCourses WHERE COURSE_ID = ?";
					st = con.prepareStatement(query);
					st.setInt(1, courseID);
					int res = st.executeUpdate();
					
					query = "DELETE FROM enrolledCourses WHERE COURSE_ID = ?";
					st = con.prepareStatement(query);
					st.setInt(1, courseID);
					res = st.executeUpdate();
					
					
					
					query = "SELECT FOLDER_ID FROM courseAndfolders WHERE COURSE_ID = ?";
					st = con.prepareStatement(query);
					st.setInt(1, courseID);
					rs = st.executeQuery();
					
					while(rs.next()) {
						query = "DELETE FROM folder WHERE ID = ?";
						PreparedStatement s2 = con.prepareStatement(query);
						s2.setString(1, rs.getString(1));
						res = s2.executeUpdate();
						
						query = "SELECT FILE_ID FROM folderAndFiles WHERE FOLDER_ID = ?";
						PreparedStatement s3 = con.prepareStatement(query);
						s3.setString(1, rs.getString(1));
						ResultSet rs2 = s3.executeQuery();
						
						while(rs2.next()) {
							// File delete
							query = "DELETE FROM file WHERE ID = ?";
							PreparedStatement st4 = con.prepareStatement(query);
							st4.setString(1, rs2.getString(1));
							res = st4.executeUpdate();
						}
						
						query = "DELETE FROM folderAndFiles WHERE FOLDER_ID = ?";
						s3 = con.prepareStatement(query);
						s3.setString(1, rs.getString(1));
						res = s3.executeUpdate();
						
					}
					
					
					
					query = "DELETE FROM courseAndfolders WHERE COURSE_ID = ?";
					st = con.prepareStatement(query);
					st.setInt(1, courseID);
					res = st.executeUpdate();
					
					query = "DELETE FROM course WHERE NAME = ?";
					st = con.prepareStatement(query);
					st.setString(1, editCourseBox.getValue());
					res = st.executeUpdate();
					
				}
			
			}catch(Exception e) {
				e.printStackTrace();
			}
		
			showCreatedCourses();
			showAllAvailableCourses();
		}
		
		
		
		
	}
	
	public void editMyCourse(MouseEvent mouseEvent) {
		
		if(mouseEvent.getClickCount() == 2) {
			TreeItem<String> item = myCoursesTreeView.getSelectionModel().getSelectedItem();
				if(item != null) {
					EditMyCourses.editMyCourse(item, myCoursesTreeView, con);
					showCreatedCourses();
					showAllAvailableCourses();
				}
								
	
			}
			
			
	}
	
	
	
	
	public void displayMyCoursesTree() {
		
		try {
			files.clear();
			folders.clear();
			int courseID = 0;
			String query = "SELECT ID FROM course WHERE NAME = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, editCourseBox.getValue());
			ResultSet rs = st.executeQuery();
			
			if(rs.next()) {
				courseID = rs.getInt(1);
			}
			String query2 = "SELECT f.ID, f.NAME FROM courseAndFolders as cf, folder as f WHERE cf.COURSE_ID = ? AND cf.FOLDER_ID = f.ID";
			st = con.prepareStatement(query2);
			st.setInt(1,courseID);
			ResultSet r2 = st.executeQuery();
			while(r2.next()) {
				String query3 = "SELECT f.NAME FROM folderAndFiles as ff, file as f WHERE ff.FOLDER_ID = ? AND ff.FILE_ID = f.ID";
				PreparedStatement st2 = con.prepareStatement(query3);
				st2.setInt(1, r2.getInt(1));
				ResultSet r3 = st2.executeQuery();
				ArrayList<String> file = new ArrayList<>();
				while(r3.next()) {
					file.add(r3.getString(1));
					
				}
				files.add(file);
				folders.add(r2.getString(2));
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		myCoursesTreeView.setRoot(EnrolledCoursesUtil.getSelectedCourse(editCourseBox.getValue(), folders, files));

		
	}

	
	// My courses ends
	
	
	// Enrolled courses
		
		public void initEnrolledCourses() {
			enrolledCourseTreeView.setRoot(null);
			enrolledCourseChoiceBox.getItems().clear();
			try {
				String query = "SELECT c.NAME FROM enrolledCourses as e, course as c WHERE e.STUDENT_ID = "+student.getId()+" AND e.COURSE_ID = c.ID;";
				
				Statement s1 = con.createStatement();
				ResultSet r1 = s1.executeQuery(query);
				
				while(r1.next()) {
					enrolledCourseChoiceBox.getItems().add(new String(r1.getString(1)));
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			
	
		}
	
	
	
		public void showEnrolledCourse() {
			
			try {
				files.clear();
				folders.clear();
				int courseID = 0;
				String query = "SELECT ID FROM course WHERE NAME = ?";
				PreparedStatement st = con.prepareStatement(query);
				st.setString(1, enrolledCourseChoiceBox.getValue());
				ResultSet rs = st.executeQuery();
				
				if(rs.next()) {
					courseID = rs.getInt(1);
				}
				String query2 = "SELECT f.ID, f.NAME FROM courseAndFolders as cf, folder as f WHERE cf.COURSE_ID = ? AND cf.FOLDER_ID = f.ID";
				st = con.prepareStatement(query2);
				st.setInt(1,courseID);
				ResultSet r2 = st.executeQuery();
				while(r2.next()) {
					String query3 = "SELECT f.NAME FROM folderAndFiles as ff, file as f WHERE ff.FOLDER_ID = ? AND ff.FILE_ID = f.ID";
					PreparedStatement st2 = con.prepareStatement(query3);
					st2.setInt(1, r2.getInt(1));
					ResultSet r3 = st2.executeQuery();
					ArrayList<String> file = new ArrayList<>();
					while(r3.next()) {
						file.add(r3.getString(1));
						
					}
					files.add(file);
					folders.add(r2.getString(2));
				}
				
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			enrolledCourseTreeView.setRoot(EnrolledCoursesUtil.getSelectedCourse(enrolledCourseChoiceBox.getValue(), folders, files));

			
			showEnrolledCourseInformation();
			
			
			
		}
	
	public void showEnrolledCourseInformation() {
		
		try {
			
			String query = "SELECT a.NAME, a.LAST_NAME, c.START_DATE, c.END_DATE, c.PRICE FROM course as c, admins as a WHERE c.NAME = ? AND c.CREATOR_ID = a.ID";
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, enrolledCourseChoiceBox.getValue());
			ResultSet rs = st.executeQuery();
			
			if(rs.next()) {
				enrolledCourseAdmin.setText("Admin: " + rs.getString(1) + " " + rs.getString(2));
				enrolledCourseStartDate.setText("Start date: " + rs.getString(3));
				enrolledCourseEndDate.setText("Start date: " + rs.getString(4));
				enrolledCoursePrice.setText("Price: " + rs.getString(5));
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void mouseClick(MouseEvent mouseEvent) {
		
		
		if(mouseEvent.getClickCount() == 2) {
			TreeItem<String> item = enrolledCourseTreeView.getSelectionModel().getSelectedItem();
			try {
				if(item != null && !item.getParent().equals(null)) {
				
				
					
					String query = "SELECT DATE_CREATED FROM folder WHERE NAME = ?";
					PreparedStatement st = con.prepareStatement(query);
					st.setString(1, item.getValue());
					
					ResultSet rs = st.executeQuery();
					if(rs.next()) {
						// Selected folder
						
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Folder information");
						alert.setHeaderText(null);
						alert.setContentText("Name: " + item.getValue() + "\nCreated: "+ rs.getString(1));
						alert.showAndWait();
						
						
						
					}else {
						// Selected file
						String query2 = "SELECT DATE_ADDED, LINK FROM file WHERE NAME = ?";
						st = con.prepareStatement(query2);
						st.setString(1, item.getValue());
						rs = st.executeQuery();
						
						if(rs.next()) {
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("File information");
							alert.setHeaderText(null);
							alert.setContentText("Name: " + item.getValue() + "\nCreated: "+ rs.getString(1)+"\nResourse: "+ rs.getString(2));
							alert.showAndWait();
						}
						
					}
					
					
				
				
				}
			}catch(Exception e) {
			
			}
			
		}
		
		
	}
	
	
	public void leaveSelectedCourse() {
		
		if(enrolledCourseChoiceBox.getValue().equals(null)) {
			return;
		}
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm leaving course");
		alert.setHeaderText(null);
		alert.setContentText("You sure want to leave " + enrolledCourseChoiceBox.getValue());

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() != ButtonType.OK){
		    return;
		}
		
		
		try {
			int courseID = 0;
			String query = "SELECT ID FROM course WHERE NAME = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, enrolledCourseChoiceBox.getValue());
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				courseID = rs.getInt(1);
			}
			String query2 = "DELETE FROM enrolledCourses WHERE STUDENT_ID = ? AND COURSE_ID = ?";
			
			st = con.prepareStatement(query2);
			st.setInt(1, student.getId());
			st.setInt(2, courseID);
			int res = st.executeUpdate();
			
			if(res > 0) {
				alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Success!");
				alert.setHeaderText(null);
				alert.setContentText("You left " +enrolledCourseChoiceBox.getValue() + " course!");
				alert.showAndWait();
			}
			
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			enrolledCourseChoiceBox.getSelectionModel().selectFirst();
		}catch(Exception e) {
			
		}
		enrolledCourseAdmin.setText("Admin: ");
		enrolledCourseStartDate.setText("Start date: ");
		enrolledCourseEndDate.setText("Start date: ");
		enrolledCoursePrice.setText("Price: ");
		initEnrolledCourses();
	}
	
	
	// Enrolled courses ends
	
	
	
	
	
	
	
	// Account information 
	public void initInfo() {
		changeChoice.getSelectionModel().selectFirst();
		textChange1.setText("");
		textChange2.setText("");
		textChange3.setText("");
		confirmChange.setVisible(true);
		change2.setVisible(false);
		change3.setVisible(false);
		textChange3.setVisible(false);
		textChange2.setVisible(false);
		change1.setVisible(true);
		textChange1.setVisible(true);
		
		
		
		if(student.getId() != 0) {
			String query = "SELECT NAME, LAST_NAME, PASS, EMAIL, CREDIT_CARD_ID FROM students WHERE ID = " + student.getId();
			Statement st;
			ResultSet result;
			try {
				st = con.createStatement();
				result = st.executeQuery(query);
				if(result.next()) {
					accType.setText("Account type: student");
					name.setText("Name: " + result.getString(1));
					lastName.setText("Last name: " + result.getString(2));
					String pass = "";
					for(int i = 0; i < result.getString(3).length(); i++) {
						pass += "*";
					}
					password.setText("Password: " + pass);
					email.setText("Email: " + result.getString(4));
					if(result.getString(5) != null) {
						creditCard.setText("Credit card: verified!");
						informationPane.getChildren().add(new Circle(creditCard.getLayoutX() + 130 ,creditCard.getLayoutY()+8, 5, Color.GREEN));
					}else {
						creditCard.setText("Credit card: not found!");
						informationPane.getChildren().add(new Circle(creditCard.getLayoutX() + 130 ,creditCard.getLayoutY()+8, 5, Color.RED));
					}
					
					String totQuery = "SELECT count(c.NAME) FROM students AS s, course AS c, enrolledCourses AS e WHERE e.STUDENT_ID = s.ID AND e.COURSE_ID = c.ID AND s.ID = " +student.getId();
					Statement totalStatement = con.createStatement();
					ResultSet totalRS = totalStatement.executeQuery(totQuery);
					if(totalRS.next()) {
						totalEnrolled.setText("Total enrolled courses: " + String.valueOf(totalRS.getString(1)));
					}
					
					
					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
			
		}else {
			
			
			String query = "SELECT NAME, LAST_NAME, PASS, EMAIL, PHONE_NUMBER FROM admins WHERE ID = " + admin.getId();
			Statement st;
			ResultSet result;
			try {
				st = con.createStatement();
				result = st.executeQuery(query);
				if(result.next()) {
					accType.setText("Account type: administrator");
					name.setText("Name: " + result.getString(1));
					lastName.setText("Last name: " + result.getString(2));
					String pass = "";
					for(int i = 0; i < result.getString(3).length(); i++) {
						pass += "*";
					}
					password.setText("Password: " + pass);
					email.setText("Email: " + result.getString(4));
					creditCard.setText("Phone number: " + result.getString(5));
					
					
					String totQuery = "SELECT count(c.NAME) FROM admins AS a, course AS c, createdCourses AS e WHERE e.ADMIN_ID = a.ID AND e.COURSE_ID = c.ID AND a.ID = " + admin.getId();
					Statement totalStatement = con.createStatement();
					ResultSet totalRS = totalStatement.executeQuery(totQuery);
					if(totalRS.next()) {
						totalEnrolled.setText("Total courses created: " + String.valueOf(totalRS.getString(1)));
					}
					
					
					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			

			
			
			
		}
			
		
	}
	

	public void changeInformation() {
		change1.setVisible(true);
		textChange1.setVisible(true);
		confirmChange.setVisible(true);
		
		if(changeChoice.getValue().equals("Name")) {
			change2.setVisible(false);
			change3.setVisible(false);
			textChange3.setVisible(false);
			textChange2.setVisible(false);
			change1.setText("Enter new name");
			
		}else if(changeChoice.getValue().equals("Last name")) {
			change2.setVisible(false);
			change3.setVisible(false);
			textChange3.setVisible(false);
			textChange2.setVisible(false);
			change1.setText("Enter new last name");			
			
		}else if(changeChoice.getValue().equals("Email")) {
			change3.setVisible(false);
			textChange3.setVisible(false);
			change2.setVisible(true);
			textChange2.setVisible(true);
			change1.setText("Enter new email adress");
			change2.setText("Confirm email adress");
		
		}else if(changeChoice.getValue().equals("Password")) {
			change2.setVisible(true);
			change3.setVisible(true);
			textChange3.setVisible(true);
			textChange2.setVisible(true);
			change1.setText("Enter current password");
			change2.setText("Enter new password");
			change3.setText("Confirm new password");
		}else if(changeChoice.getValue().equals("Credit card")) {
			change2.setVisible(true);
			change3.setVisible(true);
			textChange3.setVisible(true);
			textChange2.setVisible(true);
			change1.setText("Enter card number");
			change2.setText("Enter card valid date (mm/yy)");
			change3.setText("Enter card security code");
			
		}else if(changeChoice.getValue().equals("Phone number")) {
			change2.setVisible(false);
			change3.setVisible(false);
			textChange3.setVisible(false);
			textChange2.setVisible(false);
			change1.setText("Enter new phone number");
			
		}
		
		
		
		
	}
	
	public void logOut(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm log out");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure want to log out?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    // ... user chose OK
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Login.fxml"));
			
			try {
				Parent root = loader.load();
		
				Stage stage = (Stage)logOutBtn.getScene().getWindow();
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("/view/app.css").toExternalForm());
				stage.setScene(scene);
				stage.show();
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	
	// Account information ends
	

	private void removeTabs() {
		if(student.getId() == 0) {
			tabPane.getTabs().remove(enrolledCoursesTab);
			mainEnrollButton.setVisible(false);
		}
		if(admin.getId() == 0) {
			tabPane.getTabs().remove(courseCreation);
		}
		
	}
	
	private void initChoiceBoxes() {
		
		changeChoice.getItems().addAll(new String("Name"), new String("Last name"), new String("Email"), new String("Password"));
		if(student.getId() != 0) {
			changeChoice.getItems().add(new String("Credit card"));
		}else {
			changeChoice.getItems().add(new String("Phone number"));
		}

	}
	
	
	
	public void updateInformation(ActionEvent event) {
		if(textChange1.getText().equals("")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error!");
			alert.setHeaderText(null);
			alert.setContentText("Please fill information");
			alert.showAndWait();
			return;
		}
		String queryBegin = "";
		String query = "SET ";
		String colum = "";
		switch(changeChoice.getSelectionModel().getSelectedItem()) {
		
			case "Name":
				colum = "NAME";
				query += colum;
				query += " = \"" + textChange1.getText() + "\"";
				break;
			case "Last name":
				colum = "LAST_NAME";
				query += colum;
				query += " = \"" + textChange1.getText() + "\"";
				break;
			case "Password":
				
				String table = "";
				String confirmPass = "";
				if(student.getId() != 0) {
					// Check students
					table = "students";
					confirmPass = "SELECT PASS FROM "+table+" WHERE ID = " + student.getId();
				}else {
					table = "admins";
					confirmPass = "SELECT PASS FROM "+table+" WHERE ID = " + admin.getId();
				}
				
				
				try {
					Statement checkPass = con.createStatement();
					ResultSet checkPassResult = checkPass.executeQuery(confirmPass);
					while(checkPassResult.next()) {
						if(!checkPassResult.getString(1).equals(textChange1.getText())) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Error!");
							alert.setHeaderText(null);
							alert.setContentText("Wrong current password!");
							alert.showAndWait();
							return;
						}else if( !(textChange2.getText().equals(textChange3.getText())) || ( textChange2.getText().equals("")) || ( textChange3.getText().equals("")) ) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Error!");
							alert.setHeaderText(null);
							alert.setContentText("Passwords do not match!");
							alert.showAndWait();
							return;
						}
						
						
					}
				}catch(Exception e) {
					
				}
	
				colum = "PASS";
				query += colum;
				query += " = \"" + textChange2.getText() + "\"";
				break;
			case "Email":
				
				if(!textChange1.getText().equals(textChange2.getText())) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error!");
					alert.setHeaderText(null);
					alert.setContentText("Emails do not match");
					alert.showAndWait();
					return;
				}

				colum = "EMAIL";
				query += colum;
				query += " = \"" + textChange2.getText() + "\"";
				break;
			case "Phone number":
				colum = "PHONE_NUMBER";
				query += colum;
				query += " = \"" + textChange1.getText() + "\"";
				break;
			case "Credit card":
				
				String creditQuery = "SELECT CREDIT_CARD_ID FROM students WHERE ID = ?";
				try {
					PreparedStatement s1 = con.prepareStatement(creditQuery);
					s1.setInt(1, student.getId());
					ResultSet r1 = s1.executeQuery();
					
					while(r1.next()) {

						if(r1.getInt(1) == 0) {
							// Create credit card
							
							String newCardQuery = "INSERT INTO credit_card VALUES(0,\""+textChange1.getText()+"\",\""+textChange2.getText()+"\",\""+textChange3.getText()+"\")";
							int r2 = s1.executeUpdate(newCardQuery);
							
							String newCardId = "SELECT ID FROM credit_card ORDER BY ID DESC LIMIT 1";
							ResultSet r3 = s1.executeQuery(newCardId);
							
							while(r3.next()) {
								String updateCredit = "UPDATE students SET CREDIT_CARD_ID = " + r3.getString(1) + " WHERE ID = " + student.getId();
								int r4 = s1.executeUpdate(updateCredit);
							}
							
						}else {
							// Update card
							String updateQuery = "UPDATE credit_card SET NUMBER = \"" + textChange1.getText()+"\", VALID = \"" + textChange2.getText() + "\", CVC = \"" + textChange3.getText() + "\" WHERE ID = " + r1.getInt(1);
							int r5 = s1.executeUpdate(updateQuery);
						}
						
						
						
					}
					
					
					
				}catch(Exception e) {
					
				}
				
				
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Success!");
				alert.setHeaderText(null);
				alert.setContentText(changeChoice.getSelectionModel().getSelectedItem() + " updated successfully!");
				alert.showAndWait();
				initInfo();
				

				return;
				
	
		}
		
		if(student.getId() != 0) {
			// Update students
			query += " WHERE ID = " + student.getId();
			queryBegin = "UPDATE students " + query;
		}else {
			// Update admins
			query += " WHERE ID = " + admin.getId();
			queryBegin = "UPDATE admins " + query;
			
		}
		
		try {
			Statement st = con.createStatement();
			int count = st.executeUpdate(queryBegin);
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}
		
		
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Success!");
		alert.setHeaderText(null);
		alert.setContentText(changeChoice.getSelectionModel().getSelectedItem() + " updated successfully!");
		alert.showAndWait();
		initInfo();
		
		
	}
	
	
}
