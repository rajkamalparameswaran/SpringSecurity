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
import com.isteer.module.EndPoint;
import com.isteer.module.User;
import com.isteer.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;

	@Override
	@Transactional
	public UserResponse addUser(User user) {

		List<String> exceptions = new ArrayList<String>();
		if ((user.getUserName() == null) || (user.getUserFullName() == null) || (user.getUserEmail() == null)
				|| (user.getUserPassword() == null) || (user.getUserAddresses() == null)
				|| (user.getUserRoles() == null)) {
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

				userDao.addPrivileges(userId);

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
	@Transactional
	public AlternativeReturnUser updateUser(User user) {

		if (!userDao.isIdFound(user.getUserId())) {
			List<String> exception = new ArrayList<>();
			exception.add("User Id not found");
			throw new UserIdNotFoundException(0, "data updated failed", exception);
		} else {
			List<String> exceptions = new ArrayList<String>();

			if ((user.getUserId() == 0) || (user.getUserName() == null) || (user.getUserFullName() == null)
					|| (user.getUserEmail() == null) || (user.getUserPassword() == null)
					|| (user.getUserAddresses() == null) || (user.getUserRoles() == null)
					|| user.getPrivileges() == null) {
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
				if (user.getPrivileges().isEmpty()) {

					exceptions.add("User privileges cannot be empty give no privilege");

				}

			}

			if (exceptions.isEmpty()) {
				try {
					userDao.deleteRolesById(user.getUserId());
					userDao.deleteAddressById(user.getUserId());
					userDao.deletePrivilegesById(user.getUserId());
					userDao.updateUser(user);
					userDao.addAddresses(user.getUserAddresses(), user.getUserId());
					userDao.addRoles(user.getUserRoles(), user.getUserId());
					userDao.addPrivileges(user.getPrivileges(), user.getUserId());

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
	@Transactional
	public UserResponse deleteUserById(Integer userId) {
		if (!userDao.isIdFound(userId)) {
			List<String> exception = new ArrayList<>();
			exception.add("User Id not found");
			throw new UserIdNotFoundException(0, "data deleted failed", exception);
		} else {
			userDao.deleteRolesById(userId);
			userDao.deleteAddressById(userId);
			userDao.deletePrivilegesById(userId);
			userDao.deleteUserById(userId);

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
		User user=userDao.getUserByUserName(userName);

		return user;
	}

	@Override
	public boolean isIdFound(Integer userId) {

		return userDao.isIdFound(userId);
	}

	@Override
	public String grantPermission(User user) {

		if (!userDao.isIdFound(user.getUserId())) {
			List<String> exception = new ArrayList<>();
			exception.add("User Id not found");
			throw new UserIdNotFoundException(0, "data fetched failed", exception);
		} else {

			userDao.deletePrivilegesById(user.getUserId());
			userDao.addPrivileges(user.getPrivileges(), user.getUserId());

			return "Permission Granted sucessfully";

		}

	}

	@Override
	public AddressesResponse getAddressByUserId(Integer userId) {
		List<String> exception = new ArrayList<>();
		

		if (userDao.isIdFound(userId) == false) {
			exception.add("User Id Not Found");
			throw new UserIdNotFoundException(0, "Cannot do process", exception);
		} 
			
		

		try {

			List<String> addresses = userDao.getAddressByUserId(userId);

			return new AddressesResponse(1, "Sucess", addresses);

		} catch (Exception e) {

			exception.add(e.getMessage());
			throw new SqlQueryException(0, "Cannot do process", exception);

		}

	}

	@Override
	public AddressResponse getAddressByUserIdAndAddressId(Integer userId, Integer addressId) {
		
		List<String> exception = new ArrayList<>();

		if (userDao.isIdFound(userId) == false) {
			exception.add("User Id Not Found");
		}
		
		if(userDao.addressIdFounder(addressId)==null)
		{
			exception.add("Address Id Not Found");
		}
		
		if(!exception.isEmpty())
		{
			throw new UserIdNotFoundException(0, "Cannot do process", exception);
		}
		
		try {
			
			String address=userDao.getAddressByUserIdAndAddressId(userId, addressId);
			
			
			if(address==null)
			{
				exception.add("userId and address id not match");
				throw new UserIdNotFoundException(0, "Cannot do process",exception );
			}
			
			
			return new AddressResponse(1, "Sucess", address);
			
		}catch (UserIdNotFoundException e) {
			
			throw e;
		}
		catch (Exception e) {
			exception.add(e.getMessage());
			throw new SqlQueryException(0, "Cannot do  process", exception);
		}
			
		
		
	}

	@Transactional
	@Override
	public EndPointResponse addNewEndPoint(EndPoint endPoint) {
		
		List<String> exception=new ArrayList<>();
		if (endPoint.getEndPointName()==null) {
			exception.add("Please provide Endpoint");
			
		}
		if(endPoint.getAuthorities()==null)
		{
			exception.add("Please provide authorization privileges");
		}
		
		if (exception.isEmpty()) {
			
			try {
				
				Integer EndPointId=userDao.addEndPoint(endPoint);
				endPoint.setEndPointId(EndPointId);
				userDao.addAuthorization(endPoint.getAuthorities(), EndPointId);
				
				return new EndPointResponse(1, "End point addedd sucessfully", endPoint);
				
			} catch (Exception e) {

                    throw e;
			}
			
		}else {
			throw new SqlQueryException(0, "End point added failed", exception);
		}
		


		
	}


	@Transactional
	@Override
	public EndPointResponse updateEndPointAccess(EndPoint endPoint) {
		List<String> exception=new ArrayList<>();
		String endPointName=userDao.endPointIdFounder(endPoint.getEndPointId());
		endPoint.setEndPointName(endPointName);
		
		if(endPointName==null)
		{
			exception.add("End Point Id not found");
			throw new UserIdNotFoundException(0, "Updation failed", exception);
			
		}
		
		
		if (endPoint.getAuthorities()==null) {
			exception.add("Please provide privilleges");
			
		}
		
		if(!exception.isEmpty())
		{
			throw new SqlQueryException(0, "data updated failed", exception);
		}
		try {
			
			userDao.deleteAuthorization(endPoint.getEndPointId());
			userDao.addAuthorization(endPoint.getAuthorities(), endPoint.getEndPointId());
			
			return new EndPointResponse(1, "data updated sucessfull", endPoint);
			
		} catch (Exception e) {

			exception.add(e.getMessage());
             throw new SqlQueryException(0, "data updated failed", exception);
		}
		
		
		
	}

	@Override
	public List<EndPoint> getAllEndPointDetails() {
		
		return userDao.getAllEndPointDetails();
	}

}
