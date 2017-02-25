package com.drivecloud.web.DAO.Impl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;

import com.drivecloud.web.DAO.FileDAO;
import com.drivecloud.web.model.FileCloud;
import com.drivecloud.web.model.FolderCloud;

public class JdbcFilecloudDAO implements FileDAO {
	private DataSource dataSource;
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public void insert(FileCloud fileCloud){

		String sql = "INSERT INTO FileCloud " +
				"(FOLDERID, FILEID, FILENAME, USERID, LINK,CLOUDTYPE,SIZE) VALUES (?, ?, ?,?,?,?,?)";
		Connection conn = null;

		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, fileCloud.getfolderID());
			ps.setString(2, fileCloud.getFileID());
			ps.setString(3, fileCloud.getFileName());
			ps.setInt(4, fileCloud.getuserID());
			ps.setString(5, fileCloud.getLink());
			ps.setInt(6, fileCloud.getCloudType());
			ps.setDouble(7, fileCloud.getSize());
			ps.executeUpdate();
			ps.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
	}
	public void NewFolder(FolderCloud foldercloud){

		String sql = "INSERT INTO FOLDER " +
				"(PID, USERID, NAME) VALUES (?, ?,?)";
		Connection conn = null;

		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, foldercloud.getPID());
			ps.setInt(2, foldercloud.getUserID());
			ps.setString(3, foldercloud.getName());
			ps.executeUpdate();
			ps.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
	}
	public List<FileCloud> list(int folderID, int UserID) {
		String sql = "SELECT * FROM FileCloud where FOLDERID = ? AND USERID = ?";
		Connection conn = null;
		try {
			List<FileCloud> FileList = new ArrayList<FileCloud>();
			conn = dataSource.getConnection();
			//PreparedStatement ps = conn.prepareStatement(sql);
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1,folderID);
			ps.setInt(2,UserID);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				FileCloud fileCloudtmp = new FileCloud(rs.getInt(1),
						rs.getString(2),
						rs.getString(4),
						rs.getInt(3),
						rs.getString(5),
						rs.getInt(6),
						rs.getDouble(7));
				FileList.add(fileCloudtmp);
			}
			return FileList;
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
	}
	public List<FileCloud> listallfile(int UserID) {
		String sql = "SELECT * FROM FileCloud where USERID = ?";
		Connection conn = null;
		try {
			List<FileCloud> FileList = new ArrayList<FileCloud>();
			conn = dataSource.getConnection();
			//PreparedStatement ps = conn.prepareStatement(sql);
			PreparedStatement ps = conn.prepareStatement(sql);
			//ps.setInt(1,folderID);
			ps.setInt(1,UserID);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				FileCloud fileCloudtmp = new FileCloud(rs.getInt(1),
						rs.getString(2),
						rs.getString(4),
						rs.getInt(3),
						rs.getString(5),
						rs.getInt(6),
						rs.getDouble(7));
				FileList.add(fileCloudtmp);
			}
			return FileList;
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
	}	
	public List<FolderCloud> ListFolder(int folderID, int UserID) {
		String sql = "SELECT * FROM FOLDER where PID = ? AND USERID = ?";
		Connection conn = null;
		try {
			List<FolderCloud> FolderList = new ArrayList<FolderCloud>();
			conn = dataSource.getConnection();
			//PreparedStatement ps = conn.prepareStatement(sql);
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1,folderID);
			ps.setInt(2,UserID);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				FolderCloud folderCloudtmp = new FolderCloud(rs.getInt(1),
						rs.getInt(2),
						rs.getInt(3),
						rs.getString(4));
				FolderList.add(folderCloudtmp);
			}
			return FolderList;
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
	}

	public List<FolderCloud> ListAllFolder(int UserID) {
		String sql = "SELECT * FROM FOLDER where USERID = ?";
		Connection conn = null;
		try {
			List<FolderCloud> FolderList = new ArrayList<FolderCloud>();
			conn = dataSource.getConnection();
			//PreparedStatement ps = conn.prepareStatement(sql);
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1,UserID);
			//ps.setInt(2,UserID);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				FolderCloud folderCloudtmp = new FolderCloud(rs.getInt(1),
						rs.getInt(2),
						rs.getInt(3),
						rs.getString(4));
				FolderList.add(folderCloudtmp);
			}
			return FolderList;
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
	}

	public void DeleFile(int UserID, String Fileid) {
		String sql = "DELETE FROM FileCloud where FILEID = ? AND USERID = ?";
		Connection conn = null;
		try {
			List<FolderCloud> FolderList = new ArrayList<FolderCloud>();
			conn = dataSource.getConnection();
			//PreparedStatement ps = conn.prepareStatement(sql);
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1,Fileid);
			ps.setInt(2,UserID);
			ps.executeUpdate();
			ps.close();

		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
	}
	public void DeleFileWithfolder(int UserID, int folderid) {
		String sql = "DELETE FROM FileCloud where FOLDERID = ? AND USERID = ?";
		Connection conn = null;
		try {
			List<FolderCloud> FolderList = new ArrayList<FolderCloud>();
			conn = dataSource.getConnection();
			//PreparedStatement ps = conn.prepareStatement(sql);
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1,folderid);
			ps.setInt(2,UserID);
			ps.executeUpdate();
			ps.close();

		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
	}
	public void DeleteFolder(int UserID, int folderid) {
		String sql = "DELETE FROM FOLDER where (ID = ? OR PID = ?) AND USERID = ?";
		Connection conn = null;
		try {
			List<FolderCloud> FolderList = new ArrayList<FolderCloud>();
			conn = dataSource.getConnection();
			//PreparedStatement ps = conn.prepareStatement(sql);
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1,folderid);
			ps.setInt(2,folderid);
			ps.setInt(3,UserID);
			ps.executeUpdate();
			ps.close();

		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
	}
	
	public void ChangeFFolderID(String Fileid, int folderid, int userid ) {
		String sql = "UPDATE FileCloud SET FOLDERID = ? WHERE FILEID = ? AND USERID = ?";
		Connection conn = null;
		try {
			List<FolderCloud> FolderList = new ArrayList<FolderCloud>();
			conn = dataSource.getConnection();
			//PreparedStatement ps = conn.prepareStatement(sql);
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1,folderid);
			ps.setString(2,Fileid);
			ps.setInt(3,userid);
			ps.executeUpdate();
			ps.close();

		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
	}
	
	public void ChangeFilename(String Fileid, String FileName, int userid ) {
		String sql = "UPDATE FileCloud SET FILENAME = ? WHERE FILEID = ? AND USERID = ?";
		Connection conn = null;
		try {
			List<FolderCloud> FolderList = new ArrayList<FolderCloud>();
			conn = dataSource.getConnection();
			//PreparedStatement ps = conn.prepareStatement(sql);
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1,FileName);
			ps.setString(2,Fileid);
			ps.setInt(3,userid);
			ps.executeUpdate();
			ps.close();

		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}
			}
		}
	}
	
}
