package com.drivecloud.web.DAO;

import java.util.List;

import com.drivecloud.web.model.FileCloud;

public interface FileDAO {

		public void insert(FileCloud fileCloud);
//		public FileCloud findByCustomerId(int fileID);
		public List<FileCloud> list(int folderID, int UserID);

}
