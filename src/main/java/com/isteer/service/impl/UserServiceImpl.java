package com.isteer.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isteer.dao.layer.UserDao;
import com.isteer.exception.SqlQueryException;
import com.isteer.exception.UserIdNotFoundException;
import com.isteer.module.User;
import com.isteer.services.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;

	@Override
	public UserResponse addUser(User user) {

		List<String> exceptions = new ArrayList<String>();
		if ((user.getUserName() == null) || (user.getUserFullName() == null) || (user.getUserEmail() == null)
				|| (user.getUserPassword() == null)||(user.getUserAddresses()==null)||(user.getUserRoles()==null)) {
			exceptions.add("Please provide valid data");

		} else {
			if (user.getUserAddresses().isEmpty()) {
				exceptions.add("User address cannot be empty");

			}
			if (user.getUserRoles().isEmpty()) {
				exceptions.add("User roles cannot be empty");

			}

			if (!(user.getUserName().length() > 3 && user.getUserName().length() <= 10)) {
				exceptions.add("User Name length is invalid");
			}
			Map<String, Boolean> duplicates = userDao.duplicateEntry(user);
			if (duplicates.get("userName") == true) {
				exceptions.add("UserName Already Exist");

			}

			if (duplicates.get("userEmail") == true) {
				exceptions.add("User Email Exist");
			}
		}

		if (exceptions.isEmpty()) {
			try {
				Integer userId = userDao.addUser(user);
				user.setUserId(userId);
				userDao.addAddresses(user.getUserAddresses(), userId);
				userDao.addRoles(user.getUserRoles(), userId);
				ReturnUser returnUser = new ReturnUser(userId, user.getUserName(), user.getUserFullName(),
						user.getUserEmail(), user.getUserPassword(), user.getUserAddresses(), user.getUserRoles());

				return new UserResponse(1, "Data added sucessfully", returnUser);

			} catch (Exception e) {

				exceptions.add(e.getMessage());

				throw new SqlQueryException(0, "Data added failed", exceptions);
			}

		} else {
			throw new SqlQueryException(0, "Data added failed", exceptions);

		}

	}

	@Override
	public AlternativeReturnUser updateUser(User user) {

		if (!userDao.isIdFound(user.getUserId())) {
			List<String> exception = new ArrayList<>();
			exception.add("User Id not found");
			throw new UserIdNotFoundException(0, "data updated failed", exception);
		} else {
			List<String> exceptions = new ArrayList<String>();

			if ((user.getIsAccountNonExpired() == null) || (user.getIsAccountNonLocked() == null)
					|| (user.getIsCredentialsNonExpired() == null) || (user.getIsEnabled() == null)
					|| (user.getUserId() == 0) || (user.getUserName() == null) || (user.getUserFullName() == null)
					|| (user.getUserEmail() == null) || (user.getUserPassword() == null)||(user.getUserAddresses()==null)||user.getUserRoles()==null) {
				exceptions.add("Please provide valid data");

			} else {
				if (!(user.getUserName().length() > 3 && user.getUserName().length() <= 10)) {
					exceptions.add("User Name length is invalid");
				}
				Map<String, Boolean> duplicates = userDao.duplicateEntry(user);
				if (duplicates.get("userName") == true) {
					exceptions.add("UserName Already Exist");

				}

				if (duplicates.get("userEmail") == true) {
					exceptions.add("User Email Exist");
				}
				if (user.getUserAddresses().isEmpty()) {
					exceptions.add("User address cannot be empty");

				}
				if (user.getUserRoles().isEmpty()) {
					exceptions.add("User roles cannot be empty");

				}
			}

			if (exceptions.isEmpty()) {
				try {
					userDao.deleteRolesById(user.getUserId());
					userDao.deleteAddressById(user.getUserId());
					userDao.updateUser(user);
					userDao.addAddresses(user.getUserAddresses(), user.getUserId());
					userDao.addRoles(user.getUserRoles(), user.getUserId());

					ReturnUser returnUser = new ReturnUser(user.getUserId(), user.getUserName(), user.getUserFullName(),
							user.getUserEmail(), user.getUserPassword(), user.getUserAddresses(), user.getUserRoles());

					return new AlternativeReturnUser(1, "Data updated sucessfully", user);

				} catch (Exception e) {

					exceptions.add(e.getMessage());

					throw new SqlQueryException(0, "Data updated failed", exceptions);

				}
			} else {
				throw new SqlQueryException(0, "Data updated failed", exceptions);

			}

		}

	}

	@Override
	public UserResponse deleteUserById(Integer userId) {
		if (!userDao.isIdFound(userId)) {
			List<String> exception = new ArrayList<>();
			exception.add("User Id not found");
			throw new UserIdNotFoundException(0, "data deleted failed", exception);
		} else {
			userDao.deleteRolesById(userId);
			userDao.deleteRolesById(userId);
			userDao.deleteAddressById(userId);

			return new UserResponse(1, "Data deleted sucessfully", new ReturnUser());
		}
	}

	@Override
	public UserResponse getUserById(Integer userId) {

		if (!userDao.isIdFound(userId)) {
			List<String> exception = new ArrayList<>();
			exception.add("User Id not found");
			throw new UserIdNotFoundException(0, "data fetched failed", exception);
		} else {
			User user = userDao.getUserById(userId);
			ReturnUser returnUser = new ReturnUser(user.getUserId(), user.getUserName(), user.getUserFullName(),
					user.getUserEmail(), user.getUserPassword(), user.getUserAddresses(), user.getUserRoles());

			return new UserResponse(1, "Data fetched sucessfully", returnUser);
		}

	}

	@Override
	public List<Map<String, Object>> getAllUser() {

		return userDao.getAllUsers();
	}

	@Override
	public User getUserByUserName(String userName) {
		
		return userDao.getUserByUserName(userName);
	}

	@Override
	public boolean isIdFound(Integer userId) {
		
		return userDao.isIdFound(userId);
	}

}
