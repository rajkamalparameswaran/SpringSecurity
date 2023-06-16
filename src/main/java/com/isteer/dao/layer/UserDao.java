package com.isteer.dao.layer;

import java.util.List;
import java.util.Map;

import com.isteer.module.User;

public interface UserDao {
	
	public boolean isIdFound(Integer userId);
	public Integer addUser(User user);
	public void addAddresses(List<String> userAddresses,Integer userId);
	public void addRoles(List<String> userRoles,Integer userId);
	public void updateUser(User user);
	public void deleteUserById(Integer userId);
	public void deleteAddressById(Integer userId);
	public void deleteRolesById(Integer userId);
	
	public User getUserById(Integer userId);
	public List<Map<String,Object>> getAllUsers();
	public User getUserByUserName(String userName);

	public Map<String,Boolean> duplicateEntry(User user);
	

}
