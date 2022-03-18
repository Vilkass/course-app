package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class File implements Serializable {

	private int id;
	private String name;
	private LocalDate dateAdded;
	private String linkToFile;

	public File(String name, String linkToFile) {
		super();
		this.name = name;
		this.linkToFile = linkToFile;
	}
	public File(int id, String name, LocalDate dateAdded, String linkToFile) {
		super();
		this.id = id;
		this.name = name;
		this.dateAdded = dateAdded;
		this.linkToFile = linkToFile;
	}
	
	
	public void printFileInfo() {
		System.out.print("\n\t#\t#########################################" + "\n\t#\t#	File: " + name + "\n\t#\t#	Created: "
				+ dateAdded + "\n\t#\t#	Resource Link: " + linkToFile);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public LocalDate getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(LocalDate dateAdded) {
		this.dateAdded = dateAdded;
	}

	public String getLinkToFile() {
		return linkToFile;
	}

	public void setLinkToFile(String linkToFile) {
		this.linkToFile = linkToFile;
	}

}
