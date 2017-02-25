package com.drivecloud.web.model;

public class FolderCloud {
	private int ID;
	private int PID;
	private int UserID;
	private String Name;
	
	public FolderCloud(int id, int pid, int userid, String name)
	{
		this.ID = id;
		this.PID = pid;
		this.UserID = userid;
		this.Name = name;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getPID() {
		return PID;
	}
	public void setPID(int pID) {
		PID = pID;
	}
	public int getUserID() {
		return UserID;
	}
	public void setUserID(int userID) {
		UserID = userID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
}
