package com.drivecloud.web.DAO;

import java.sql.SQLException;

public interface UserDao {
	 public boolean isValidUser(String username, String password) throws SQLException;
}
