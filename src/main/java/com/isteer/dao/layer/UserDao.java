package com.isteer.dao.layer;

import java.util.List;
import java.util.Map;

import com.isteer.module.EndPoint;
import com.isteer.module.User;

public interface UserDao {
	public boolean isIdFound(Integer userId);
	public Integer addUser(User user);
	public void addAddresses(List<String> userAddresses, Integer userId);
	public void addRoles(List<String> userRoles, Integer userId);
	public void addPrivileges(List<String> privileges, Integer userId);
	public void addPrivileges(Integer userId);
	public void updateUser(User user);
	public void deleteUserById(Integer userId);
	public void deleteAddressById(Integer userId);
	public void deletePrivilegesById(Integer userId);
	public void deleteRolesById(Integer userId);
	public User getUserById(Integer userId);
	public List<Map<String, Object>> getAllUsers();
	public User getUserByUserName(String userName);
	public Map<String, Boolean> duplicateEntry(User user);
	public List<String> getAddressByUserId(Integer userId);
	public String getAddressByUserIdAndAddressId(Integer userId, Integer addressId);
	public String addressIdFounder(Integer addressId);
	public Integer addEndPoint(EndPoint endPoint);
	public void addAuthorization(List<String> authorities, Integer endPointId);
	public void deleteAuthorization(Integer endPointId);
	public String endPointIdFounder(Integer endPointId);
	public List<EndPoint> getAllEndPointDetails();
}
