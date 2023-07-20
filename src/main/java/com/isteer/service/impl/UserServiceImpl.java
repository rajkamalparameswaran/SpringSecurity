package com.isteer.service.impl;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isteer.dao.layer.UserDao;
import com.isteer.exception.SqlQueryException;
import com.isteer.exception.UserIdNotFoundException;
import com.isteer.logs.Log4j2;
import com.isteer.message.properties.FailedMessage;
import com.isteer.message.properties.SuccessMessage;
import com.isteer.module.EndPoint;
import com.isteer.module.User;
import com.isteer.services.UserService;
import com.isteer.statuscode.StatusCode;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private String id = "Id :";

	@Autowired
	SuccessMessage successMsg;
	@Autowired
	FailedMessage failedMsg;

	@Autowired
	UserDao userDao;

	public List<String> getErrorList(User user) {
		List<String> exceptions = new ArrayList<>();
		if ((user.getUserName() == null) || (user.getUserFullName() == null) || (user.getUserEmail() == null)
				|| (user.getUserPassword() == null) || (user.getUserAddresses() == null)
				|| (user.getUserRoles() == null)) {
			exceptions.add(failedMsg.getNotValidData());
		} else {
			if (user.getUserAddresses().isEmpty()) {
				exceptions.add(failedMsg.getEmptyAddress());
			}
			if (!(user.getUserEmail().length() > 5 && user.getUserEmail().length() < 20)) {
				exceptions.add(failedMsg.getEmailLengthInvalid());
			}
			if (user.getUserRoles().isEmpty()) {
				exceptions.add(failedMsg.getEmptyRoles());
			}
			if (!(user.getUserName().length() > 3 && user.getUserName().length() <= 10)) {
				exceptions.add(failedMsg.getNameLengthInvalid());
			}
			try {
				if (userDao.toCheckDuplicateUserName(user.getUserName(), user.getUserId()) != 0
						|| userDao.toCheckDuplicateUserEmail(user.getUserEmail(), user.getUserId()) != 0) {
					exceptions.add(failedMsg.getNameOrEmailAlreadyExist());
				}
			} catch (SQLException e) {
				throw new SqlQueryException(StatusCode.SQLEXCEPTIONCODE.getCode(), failedMsg.getInvalidSqlQuery(),
						Arrays.asList(e.getLocalizedMessage()));
			}
		}
		return exceptions;
	}

	public void userIdFoundAndExceptionThrower(Integer userId) {
		boolean idFound;
		try {
			idFound = !userDao.isIdFound(userId);
		} catch (SQLException e) {
			throw new SqlQueryException(StatusCode.SQLEXCEPTIONCODE.getCode(), failedMsg.getInvalidSqlQuery(),
					Arrays.asList(e.getLocalizedMessage()));
		}
		if (idFound) {
			List<String> exception = new ArrayList<>();
			exception.add(failedMsg.getUserIdNotFound());

			Log4j2.getAuditlog().info(failedMsg.getUserIdNotFound()+" : User Id :"+userId);
			throw new UserIdNotFoundException(StatusCode.USERIDNOTFOUND.getCode(), failedMsg.getUserUpdationFailed(),
					exception);
		}
	}

	@Override
	public UserResponse addUser(User user) {
		List<String> exceptions = new ArrayList<>();
		Integer userId = null;
		exceptions.addAll(getErrorList(user));
		if (exceptions.isEmpty()) {
			try {
				userId = userDao.addUser(user);
				user.setUserId(userId);
				userDao.addAddresses(user.getUserAddresses(), userId);
				userDao.addRoles(user.getUserRoles(), userId);
				userDao.addPrivileges(userId);
				Log4j2.getAuditlog().info(id + " " + userId + " " + successMsg.getAccountCreated());
				ReturnUser returnUser = new ReturnUser(userId, user.getUserName(), user.getUserFullName(),
						user.getUserEmail(), user.getUserPassword(), user.getUserAddresses(), user.getUserRoles());
				return new UserResponse(StatusCode.SUCESSCODE.getCode(), successMsg.getAccountCreated(), returnUser);
			} catch (SQLException e) {
				throw new SqlQueryException(StatusCode.SQLEXCEPTIONCODE.getCode(), failedMsg.getInvalidSqlQuery(),
						Arrays.asList(e.getLocalizedMessage()));
			} catch (Exception e) {
				exceptions.add(e.getLocalizedMessage());
				Log4j2.getAuditlog().error(e.getMessage());
				throw new SqlQueryException(StatusCode.ACCOUNTCREATEDFAILED.getCode(),
						failedMsg.getAccountCreationFailed(), exceptions);
			}
		} else {
			Log4j2.getAuditlog().info(failedMsg.getNotValidData());
			throw new SqlQueryException(StatusCode.ACCOUNTCREATEDFAILED.getCode(), failedMsg.getNotValidData(),
					exceptions);
		}
	}

	@Override
	public AlternativeReturnUser updateUser(User user) {
		userIdFoundAndExceptionThrower(user.getUserId());
		List<String> exceptions = new ArrayList<>();
		if (user.getPrivileges() == null || user.getPrivileges().isEmpty()) {
			exceptions.add(failedMsg.getEmptyPrivilege() + "Give no privilege");
		}
		exceptions.addAll(getErrorList(user));
		if (exceptions.isEmpty()) {
			try {
				userDao.deleteRolesById(user.getUserId());
				userDao.deleteAddressById(user.getUserId());
				userDao.deletePrivilegesById(user.getUserId());
				userDao.updateUser(user);
				userDao.addAddresses(user.getUserAddresses(), user.getUserId());
				userDao.addRoles(user.getUserRoles(), user.getUserId());
				userDao.addPrivileges(user.getPrivileges(), user.getUserId());
				Log4j2.getAuditlog().info(id + " " + user.getUserId() + " " + successMsg.getAccountUpdated(),
						user.getUserId());
				return new AlternativeReturnUser(StatusCode.SUCESSCODE.getCode(), successMsg.getAccountUpdated(), user);
			} catch (SQLException e) {
				throw new SqlQueryException(StatusCode.SQLEXCEPTIONCODE.getCode(), failedMsg.getInvalidSqlQuery(),
						Arrays.asList(e.getLocalizedMessage()));
			} catch (Exception e) {
				exceptions.add(e.getMessage());
				Log4j2.getAuditlog().error(id + " " + user.getUserId() + " " + e.getMessage());
				throw new SqlQueryException(StatusCode.ACCOUNTUPDATEDFAILED.getCode(),
						failedMsg.getUserUpdationFailed(), exceptions);
			}
		} else {
			Log4j2.getAuditlog().error(id + " " + user.getUserId() + " " + failedMsg.getNotValidData());
			throw new SqlQueryException(StatusCode.ACCOUNTUPDATEDFAILED.getCode(), failedMsg.getUserUpdationFailed(),
					exceptions);
		}
	}

	@Override
	public Map<String, Object> deleteUserById(Integer userId) {
		userIdFoundAndExceptionThrower(userId);
		try {
			userDao.deleteRolesById(userId);
			userDao.deleteAddressById(userId);
			userDao.deletePrivilegesById(userId);
			userDao.deleteUserById(userId);
			Log4j2.getAuditlog().info(id + " " + userId + " " + successMsg.getAccountDeleted());
			Map<String, Object> response = new HashMap<>();
			response.put("UserId", userId);
			response.put("StatusCode", StatusCode.ACCOUNTDELETIONFAILED.getCode());
			response.put("Message", successMsg.getAccountDeleted());
			return response;
		} catch (SQLException e) {
			throw new SqlQueryException(StatusCode.SQLEXCEPTIONCODE.getCode(), failedMsg.getInvalidSqlQuery(),
					Arrays.asList(e.getLocalizedMessage()));
		}
	}

	@Override
	public UserResponse getUserById(Integer userId) {
		userIdFoundAndExceptionThrower(userId);
		try {
			User user = userDao.getUserById(userId);
			ReturnUser returnUser = new ReturnUser(user.getUserId(), user.getUserName(), user.getUserFullName(),
					user.getUserEmail(), user.getUserPassword(), user.getUserAddresses(), user.getUserRoles());
			Log4j2.getAuditlog().info(id + " " + userId + " " + successMsg.getAccountFetched(), userId);
			return new UserResponse(StatusCode.SUCESSCODE.getCode(), successMsg.getAccountFetched(), returnUser);
		} catch (SQLException e) {
			throw new SqlQueryException(StatusCode.SQLEXCEPTIONCODE.getCode(), failedMsg.getInvalidSqlQuery(),
					Arrays.asList(e.getLocalizedMessage()));
		}
	}

	@Override
	public List<User> getAllUser() {
		try {
			Log4j2.getAuditlog().info(successMsg.getAccountFetched());
			List<User> users = userDao.getAllUsers();
			if (users.isEmpty()) {
				Log4j2.getAuditlog().info("No Data Found");
				throw new UserIdNotFoundException(StatusCode.ACCOUNTFETCHINGFAILED.getCode(),
						failedMsg.getDataFetchProcssFailed(), Arrays.asList("NO DATA FOUND"));
			}
			return users;
		} catch (SQLException e) {
			throw new SqlQueryException(StatusCode.SQLEXCEPTIONCODE.getCode(), failedMsg.getInvalidSqlQuery(),
					Arrays.asList(e.getLocalizedMessage()));
		}
	}

	@Override
	public User getUserByUserName(String userName) {
		try {
			User user = userDao.getUserByUserName(userName);
			if(user==null) {
				Log4j2.getAuditlog().info("UserName :"+userName+" Not found");
				throw new UserIdNotFoundException(StatusCode.ACCOUNTFETCHINGFAILED.getCode(),
						failedMsg.getDataFetchProcssFailed(), Arrays.asList("NO DATA FOUND"));
			}
			Log4j2.getAuditlog().info(id + " " + user.getUserId() + " " + successMsg.getAccountFetched());
			return user;
		} catch (SQLException e) {
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SqlQueryException(StatusCode.SQLEXCEPTIONCODE.getCode(), failedMsg.getInvalidSqlQuery(),
					Arrays.asList(e.getLocalizedMessage()));
		}

	}

	@Override
	public String grantPermission(User user) {
		userIdFoundAndExceptionThrower(user.getUserId());
		try {
			userDao.deletePrivilegesById(user.getUserId());
			userDao.addPrivileges(user.getPrivileges(), user.getUserId());
			String message = MessageFormat.format("Permission provited to the userId :{0}", user.getUserId());
			Log4j2.getAuditlog().info(message);
			return message;
		} catch (SQLException e) {
			throw new SqlQueryException(StatusCode.SQLEXCEPTIONCODE.getCode(), failedMsg.getInvalidSqlQuery(),
					Arrays.asList(e.getLocalizedMessage()));
		} catch (Exception e) {
			Log4j2.getAuditlog().error(id + " " + user.getUserId() + " " + e.getLocalizedMessage());
			throw new SqlQueryException(StatusCode.ACCOUNTCREATEDFAILED.getCode(), failedMsg.getProcessFailed(),
					Arrays.asList(e.getMessage()));
		}
	}

	@Override
	public AddressesResponse getAddressByUserId(Integer userId) {
		List<String> exception = new ArrayList<>();
		userIdFoundAndExceptionThrower(userId);
		try {
			List<String> addresses = userDao.getAddressByUserId(userId);
			Log4j2.getAuditlog().info(id + " " + userId + " " + successMsg.getAccountFetched());
			return new AddressesResponse(StatusCode.SUCESSCODE.getCode(), successMsg.getAccountFetched(), addresses);
		} catch (SQLException e) {
			throw new SqlQueryException(StatusCode.SQLEXCEPTIONCODE.getCode(), failedMsg.getInvalidSqlQuery(),
					Arrays.asList(e.getLocalizedMessage()));
		} catch (Exception e) {
			exception.add(e.getMessage());
			Log4j2.getAuditlog().error(id + " " + userId + " " + e.getLocalizedMessage());
			throw new SqlQueryException(StatusCode.ACCOUNTFETCHINGFAILED.getCode(),
					failedMsg.getDataFetchProcssFailed(), exception);
		}
	}

	@Override
	public AddressResponse getAddressByUserIdAndAddressId(Integer userId, Integer addressId) {
		List<String> exception = new ArrayList<>();
		userIdFoundAndExceptionThrower(userId);
		try {
			if (userDao.addressIdFounder(addressId) == null) {
				exception.add(failedMsg.getUserIdNotFound());
			}
		} catch (SQLException e) {
			throw new SqlQueryException(StatusCode.SQLEXCEPTIONCODE.getCode(), failedMsg.getInvalidSqlQuery(),
					Arrays.asList(e.getLocalizedMessage()));
		}
		if (!exception.isEmpty()) {
			throw new UserIdNotFoundException(StatusCode.USERIDNOTFOUND.getCode(), failedMsg.getDataFetchProcssFailed(),
					exception);
		}
		try {
			String address = userDao.getAddressByUserIdAndAddressId(userId, addressId);
			if (address == null) {
				exception.add("userId and address id not match");
				Log4j2.getAuditlog().info("userId and address id not match");
				throw new UserIdNotFoundException(StatusCode.ACCOUNTFETCHINGFAILED.getCode(),
						failedMsg.getDataFetchProcssFailed(), exception);
			}
			return new AddressResponse(StatusCode.SUCESSCODE.getCode(), successMsg.getAccountFetched(), address);
		} catch (UserIdNotFoundException e) {
			throw e;
		} catch (Exception e) {
			exception.add(e.getMessage());
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SqlQueryException(StatusCode.ACCOUNTFETCHINGFAILED.getCode(),
					failedMsg.getDataFetchProcssFailed(), exception);
		}
	}

	@Override
	public EndPointResponse addNewEndPoint(EndPoint endPoint) {
		List<String> exception = new ArrayList<>();
		if (endPoint.getEndPointName() == null || endPoint.getAuthorities() == null) {
			exception.add(failedMsg.getNotValidData());
		}
		if (exception.isEmpty()) {
			try {
				Integer endPointId = userDao.addEndPoint(endPoint);
				endPoint.setEndPointId(endPointId);
				userDao.addAuthorization(endPoint.getAuthorities(), endPointId);
				Log4j2.getAuditlog().info(id + " " + endPointId + " End Point Added sucessfully", endPointId);
				return new EndPointResponse(StatusCode.SUCESSCODE.getCode(), successMsg.getEndPointAdded(), endPoint);
			} catch (SQLException e) {
				throw new SqlQueryException(StatusCode.SQLEXCEPTIONCODE.getCode(), failedMsg.getInvalidSqlQuery(),
						Arrays.asList(e.getLocalizedMessage()));
			} catch (Exception e) {
				Log4j2.getAuditlog().error(e.getLocalizedMessage());
				throw new SqlQueryException(StatusCode.ENDPOINTACESSUPDATEDFAILED.getCode(),
						failedMsg.getEndPointCannotAdded(), Arrays.asList(e.getMessage()));
			}
		} else {
			Log4j2.getAuditlog().info(failedMsg.getNotValidData());
			throw new SqlQueryException(StatusCode.ENDPOINTADDEDFAILED.getCode(), failedMsg.getEndPointCannotAdded(),
					exception);
		}
	}

	@Override
	public EndPointResponse updateEndPointAccess(EndPoint endPoint) {
		List<String> exception = new ArrayList<>();
		String endPointName = null;
		try {
			endPointName = userDao.endPointIdFounder(endPoint.getEndPointId());
		} catch (SQLException e) {
			throw new SqlQueryException(StatusCode.SQLEXCEPTIONCODE.getCode(), failedMsg.getInvalidSqlQuery(),
					Arrays.asList(e.getLocalizedMessage()));
		}
		endPoint.setEndPointName(endPointName);
		if (endPointName == null) {
			exception.add(failedMsg.getUserIdNotFound());
			Log4j2.getAuditlog().info(id + " " + endPoint.getEndPointId() + " " + failedMsg.getUserIdNotFound(),
					endPoint.getEndPointId());
			throw new UserIdNotFoundException(StatusCode.USERIDNOTFOUND.getCode(), failedMsg.getProcessFailed(),
					exception);
		}
		if (endPoint.getAuthorities() == null) {
			exception.add(failedMsg.getNotValidData());
		}
		if (!exception.isEmpty()) {
			Log4j2.getAuditlog().info(id + " " + endPoint.getEndPointId() + " " + failedMsg.getNotValidData(),
					endPoint.getEndPointId());
			throw new SqlQueryException(StatusCode.ENDPOINTACESSUPDATEDFAILED.getCode(),
					failedMsg.getUserUpdationFailed(), exception);
		}
		try {
			userDao.deleteAuthorization(endPoint.getEndPointId());
			userDao.addAuthorization(endPoint.getAuthorities(), endPoint.getEndPointId());
			Log4j2.getAuditlog().info(id + " " + endPoint.getEndPointId() + " " + successMsg.getAccountUpdated(),
					endPoint.getEndPointId());
			return new EndPointResponse(StatusCode.SUCESSCODE.getCode(), successMsg.getAccountUpdated(), endPoint);
		} catch (SQLException e) {
			throw new SqlQueryException(StatusCode.SQLEXCEPTIONCODE.getCode(), failedMsg.getInvalidSqlQuery(),
					Arrays.asList(e.getLocalizedMessage()));
		} catch (Exception e) {
			exception.add(e.getMessage());
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SqlQueryException(StatusCode.ENDPOINTACESSUPDATEDFAILED.getCode(),
					failedMsg.getUserUpdationFailed(), exception);
		}
	}

	@Override
	public List<EndPoint> getAllEndPointDetails() {
		try {

			List<EndPoint> endPoints = userDao.getAllEndPointDetails();
			if (endPoints.isEmpty()) {
				Log4j2.getAuditlog().info("No End Point Found");
				throw new UserIdNotFoundException(StatusCode.ACCOUNTFETCHINGFAILED.getCode(),
						failedMsg.getDataFetchProcssFailed(), Arrays.asList("NO DATA FOUND"));
			}
			Log4j2.getAuditlog().info("End Point Fetched Sucessfully");
			return endPoints;
		} catch (SQLException e) {
			throw new SqlQueryException(StatusCode.SQLEXCEPTIONCODE.getCode(), failedMsg.getInvalidSqlQuery(),
					Arrays.asList(e.getLocalizedMessage()));
		}
	}

}
