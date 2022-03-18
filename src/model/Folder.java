package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Folder implements Serializable {

	private int id;
	private String name;
	private LocalDate createdDate;
	private ArrayList<File> folderFiles = new ArrayList<>();

	public Folder(String name) {
		super();
		this.name = name;

	}

	public Folder(int id, String name, LocalDate createdDate) {
		super();
		this.id = id;
		this.name = name;
		this.createdDate = createdDate;
	}

	
	public void printFolderInfo() {
		System.out.print("\n\t#################################################\n" + "\t#	Folder: " + name + "\n\t#	Created: "
				+ createdDate + "\n\t#	Files: ");
		for(File f : folderFiles) {
			f.printFileInfo();
		}
		
		System.out.print(
				"\n\t#\t#########################################\n"
				+ "\t#\n\t#################################################\n");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}
	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}
	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	public ArrayList<File> getFolderFiles() {
		return folderFiles;
	}

	public void setFolderFiles(ArrayList<File> folderFiles) {
		this.folderFiles = folderFiles;
	}

}
