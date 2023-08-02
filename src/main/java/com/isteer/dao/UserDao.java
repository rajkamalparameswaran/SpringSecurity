package com.isteer.dao;

import java.sql.SQLException;
import java.util.List;

import com.isteer.module.EndPoint;
import com.isteer.module.User;

public interface UserDao {
	public boolean isIdFound(Integer userId) throws SQLException;

	public Integer addUser(User user) throws SQLException;

	public void addAddresses(List<String> userAddresses, Integer userId) throws SQLException;

	public void addRoles(List<String> userRoles, Integer userId) throws SQLException;

	public void addPrivileges(List<String> privileges, Integer userId) throws SQLException;

	public void addPrivileges(Integer userId) throws SQLException;

	public void updateUserByUser(User user) throws SQLException;
	
	public void updateUserByAdmin(User user) throws SQLException;

	public void deleteUserById(Integer userId) throws SQLException;

	public void deleteAddressById(Integer userId) throws SQLException;

	public void deletePrivilegesById(Integer userId) throws SQLException;

	public void deleteRolesById(Integer userId) throws SQLException;

	public User getUserById(Integer userId) throws SQLException;

	public List<User> getAllUsers() throws SQLException;

	public User getUserByUserName(String userName) throws SQLException;

	public List<String> getAddressByUserId(Integer userId) throws SQLException;

	public List<String> getAuthoriesByUserId(Integer userId) throws SQLException;

	public List<String> getPrivilegesByUserId(Integer userId) throws SQLException;

	public String getAddressByUserIdAndAddressId(Integer userId, Integer addressId) throws SQLException;

	public String addressIdFounder(Integer addressId) throws SQLException;

	public Integer addEndPoint(EndPoint endPoint) throws SQLException;

	public void addAuthorization(List<String> authorities, Integer endPointId) throws SQLException;

	public void deleteAuthorization(Integer endPointId) throws SQLException;

	public String endPointIdFounder(Integer endPointId) throws SQLException;

	public List<EndPoint> getAllEndPointDetails() throws SQLException;

	public int toCheckDuplicateUserName(String userName, int userId) throws SQLException;

	public int toCheckDuplicateUserEmail(String userEmail, int userId) throws SQLException;
	
	public void addValidToken(String jwt,String issuedTime,String expiredTime) throws SQLException;
	
	public boolean tokenIsValid(String jwt);
	
	public void deleteValidToken(String jwt) throws SQLException;
}
