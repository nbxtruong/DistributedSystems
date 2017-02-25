package com.drivecloud.web.DAO.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.drivecloud.web.DAO.UserDao;
import com.mysql.jdbc.PreparedStatement;

public class JdbcUserDAO implements UserDao  {

	DataSource dataSource ;

	public DataSource getDataSource()
	{
		return this.dataSource;
	}

	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	@Override
	public boolean isValidUser(String username, String password) throws SQLException
	{
		String query = "Select count(1) from USER where NAME = ? and PASSWORD = ?";
		PreparedStatement pstmt = (PreparedStatement) dataSource.getConnection().prepareStatement(query);
		pstmt.setString(1, username);
		pstmt.setString(2, password);
		ResultSet resultSet = pstmt.executeQuery();
		if(resultSet.next())
		    return (resultSet.getInt(1) > 0);
        else
           return false;
		}
	
	public int GetUserID(String username) throws SQLException
	{
		System.out.println("GetUserID Username "+username);
		String query = "Select * from USER where NAME = ?";
		PreparedStatement pstmt = (PreparedStatement) dataSource.getConnection().prepareStatement(query);
		pstmt.setString(1, username);
		ResultSet resultSet = pstmt.executeQuery();
		if(resultSet.next())
		{
			System.out.println("ID2 "+resultSet.getInt("ID"));
		    return (resultSet.getInt("ID"));
		}
        else
        	return -1;
	}

}
