package com.drivecloud.web.DAO;

import com.drivecloud.web.model.FileCloud;

import java.util.List;

public interface FileDAO {

    public void insert(FileCloud fileCloud);

    //		public FileCloud findByCustomerId(int fileID);
    public List<FileCloud> list(int folderID, int UserID);

}
