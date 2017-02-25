package com.drivecloud.web.model;

public class FileCloud {
	int FolderID;
	String FileID;
	String FileName;
	int UserID;
	int CloudType; //1: google, 2: onedrive, 3: dropbox
	String link;
	double size;

	public FileCloud(int folderID, String fileID, String fileName, int userID,String link,int cltype,double size)
	{
		this.FolderID = folderID;
		this.FileID = fileID;
		this.FileName = fileName;
		this.UserID = userID;
		this.link = link;
		this.CloudType = cltype;
		this.size = size;
	}
	public double getSize() {
		return size;
	}
	public void setSize(long size) {
		size = size;
	}
	public int getCloudType() {
		return CloudType;
	}
	public void setCloudType(int cloudType) {
		CloudType = cloudType;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public int getfolderID()
	{
		return this.FolderID;
	}
	public String getFileID()
	{
		return this.FileID;
	}
	public String getFileName()
	{
		return this.FileName;
	}
	public int getuserID()
	{
		return this.UserID;
	}
}
