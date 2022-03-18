package utils;

import java.util.ArrayList;

import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Course;
import model.File;
import model.Folder;

public class EnrolledCoursesUtil {
	
	private static Image folderIcon = new Image("/img/icon.png");
	private static Image fileIcon = new Image("/img/file.png");
	private static Image courseIcon = new Image("/img/course.png");
	
	
	public static TreeItem<String> getSelectedCourse(String courseName, ArrayList<String> folders,  ArrayList<ArrayList<String>> files) {
		
		
		TreeItem<String> root = new TreeItem<>(courseName, new ImageView(courseIcon));
		root.setExpanded(true);
		int index = 0;
		for(String fol : folders) {
			TreeItem<String> folder = new TreeItem<>(fol, new ImageView(folderIcon));
			
			for(String fil : files.get(index)) {
				TreeItem<String> item = new TreeItem<>(fil, new ImageView(fileIcon));
				folder.getChildren().add(item);
			}
			index++;
			root.getChildren().add(folder);
		}
		
		
		
		return root;
		
		
	}
	
	
	
	
	
	
	
}
