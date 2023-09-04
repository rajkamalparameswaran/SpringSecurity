package com.isteer;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.isteer.dao.UserDao;
import com.isteer.exception.SqlQueryException;
import com.isteer.exception.UserIdNotFoundException;
import com.isteer.module.User;
import com.isteer.service.impl.UserResponse;
import com.isteer.services.UserService;
import com.isteer.statuscode.StatusCode;

@SpringBootTest
class SpringSecurityUserApplicationTests {

	@MockBean
	private UserDao userDao;

	@Autowired
	private UserService userService;

	@MockBean
	private Principal principal;

	User currentUser() {
		User user = new User();
		user.setUserName("kamal");
		user.setUserFullName("kamalhasan");
		user.setUserEmail("kamal@gmail.com");
		user.setUserPassword("kamal");
		user.setUserAddresses(Arrays.asList("karai", "dvk"));
		user.setUserRoles(Arrays.asList("ADMIN"));
		return user;
	}

	User returnUser() {
		User user = currentUser();
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setCredentialsNonExpired(true);
		user.setEnabled(true);
		user.setPrivileges(Arrays.asList("NoAccess"));
		return user;
	}

	@Test
	void testAddUser() throws SQLException {

		assertAll(() -> {
			when(userDao.addUser(currentUser())).thenReturn(1);
			doNothing().when(userDao).addAddresses(currentUser().getUserAddresses(), 1);
			doNothing().when(userDao).addRoles(currentUser().getUserRoles(), 1);
			doNothing().when(userDao).addPrivileges(1);
			assertTrue(userService.getErrorList(currentUser()).isEmpty());
			when(userDao.getUserByUserName(currentUser().getUserName())).thenReturn(returnUser());
			UserResponse userResponse = userService.addUser(currentUser());
			assertEquals(returnUser(), userResponse.getUser());
			assertNotNull(userResponse);
			assertEquals(StatusCode.SUCESSCODE.getCode(), userResponse.getStatusCode());
			verify(userDao, times(1)).addUser(currentUser());
			verify(userDao, times(1)).addAddresses(currentUser().getUserAddresses(), 1);
			verify(userDao, times(1)).addRoles(currentUser().getUserRoles(), 1);
			verify(userDao, times(1)).addPrivileges(1);
		}, () -> {
			when(userDao.addUser(currentUser())).thenThrow(SQLException.class);
			doThrow(SQLException.class).when(userDao).addAddresses(currentUser().getUserAddresses(), 1);
			doThrow(SQLException.class).when(userDao).addRoles(currentUser().getUserRoles(), 1);
			doThrow(SQLException.class).when(userDao).addPrivileges(1);
			doThrow(SQLException.class).when(userDao).getUserByUserName(currentUser().getUserName());
			User user = currentUser();
			assertThrows(SqlQueryException.class, () -> userService.addUser(user));
		});
	}

	@Test
	void testUpdateUserAsAdmin() throws SQLException {

		User user = returnUser();
		user.setUserEmail("kani@gmail.com");
		user.setUserId(1);
		when(userDao.toCheckDuplicateUserEmail(user.getUserEmail(), user.getUserId())).thenReturn(0);
		when(userDao.toCheckDuplicateUserEmail(user.getUserName(), user.getUserId())).thenReturn(0);
		when(userDao.isIdFound(user.getUserId())).thenReturn(true);
		when(userDao.getUserById(user.getUserId())).thenReturn(user);
		when(principal.toString()).thenReturn("ADMIN");
		doNothing().when(userDao).updateUserByAdmin(user);
		doNothing().when(userDao).deleteAddressById(user.getUserId());
		doNothing().when(userDao).addAddresses(user.getUserAddresses(), user.getUserId());
		doNothing().when(userDao).deleteRolesById(user.getUserId());
		doNothing().when(userDao).addRoles(user.getUserRoles(), user.getUserId());
		doNothing().when(userDao).deletePrivilegesById(user.getUserId());
		doNothing().when(userDao).addPrivileges(user.getPrivileges(), user.getUserId());
		UserResponse userResponse = userService.updateUser(user);
		assertEquals(user, userResponse.getUser());
		assertEquals(StatusCode.SUCESSCODE.getCode(), userResponse.getStatusCode());
		verify(userDao, times(1)).updateUserByAdmin(user);
		verify(userDao, atMost(1)).deleteAddressById(user.getUserId());
		verify(userDao, atMost(1)).addAddresses(user.getUserAddresses(), user.getUserId());
		verify(userDao, atMost(1)).deleteAddressById(user.getUserId());
		verify(userDao, atMost(1)).deleteRolesById(user.getUserId());
		verify(userDao, atMost(1)).addRoles(user.getUserRoles(), user.getUserId());
		verify(userDao, atMost(1)).deletePrivilegesById(user.getUserId());
		verify(userDao, times(1)).addPrivileges(user.getPrivileges(), user.getUserId());
	}

	@Test
	void testUpdateUserAsUser() throws SQLException {
		User user = returnUser();
		user.setUserEmail("kani@gmail.com");
		user.setUserId(1);

		when(userDao.toCheckDuplicateUserEmail(user.getUserEmail(), user.getUserId())).thenReturn(0);
		when(userDao.toCheckDuplicateUserEmail(user.getUserName(), user.getUserId())).thenReturn(0);
		when(userDao.isIdFound(user.getUserId())).thenReturn(true);
		when(userDao.getUserById(user.getUserId())).thenReturn(user);
		when(principal.getName()).thenReturn(user.getUserName());

		doNothing().when(userDao).updateUserByUser(user);
		doNothing().when(userDao).deleteAddressById(user.getUserId());
		doNothing().when(userDao).addAddresses(user.getUserAddresses(), user.getUserId());

		UserResponse userResponse = userService.updateUser(user);

		assertNotNull(userResponse);
		assertEquals(user, userResponse.getUser());
		assertEquals(StatusCode.SUCESSCODE.getCode(), userResponse.getStatusCode());
		verify(userDao, times(1)).updateUserByUser(user);
		verify(userDao, atMost(1)).deleteAddressById(user.getUserId());
		verify(userDao, atMost(1)).addAddresses(user.getUserAddresses(), user.getUserId());
	}

	@Test
	void testUpdateUserAsUserAccessDenied() throws SQLException {
		User user = returnUser();
		user.setUserEmail("kani@gmail.com");
		user.setUserId(1);
		when(userDao.toCheckDuplicateUserEmail(user.getUserEmail(), user.getUserId())).thenReturn(0);
		when(userDao.toCheckDuplicateUserEmail(user.getUserName(), user.getUserId())).thenReturn(0);
		when(userDao.isIdFound(user.getUserId())).thenReturn(true);
		when(userDao.getUserById(user.getUserId())).thenReturn(user);
		when(principal.toString()).thenReturn("USER");
		when(principal.getName()).thenReturn("Vedha");
		assertThrows(Exception.class, () -> userService.updateUser(user));
	}

	@Test
	void testDeleteUserById() throws SQLException {
		Integer userId = 1;
		when(userDao.isIdFound(userId)).thenReturn(true);
		Map<String, Object> actualResponse = userService.deleteUserById(userId);
		assertNotNull(actualResponse);
		assertEquals(StatusCode.SUCESSCODE.getCode(), actualResponse.get("StatusCode"));
		verify(userDao, times(1)).deleteRolesById(userId);
		verify(userDao, times(1)).deleteAddressById(userId);
		verify(userDao, times(1)).deletePrivilegesById(userId);
		verify(userDao, times(1)).deleteUserById(userId);
	}

	@Test
	void testDeleteUserByIdNotFound() throws SQLException {
		Integer userId = 1;
		when(userDao.isIdFound(userId)).thenReturn(false);
		assertThrows(UserIdNotFoundException.class, () -> userService.deleteUserById(userId));
		verify(userDao, never()).deleteRolesById(userId);
		verify(userDao, never()).deleteAddressById(userId);
		verify(userDao, never()).deletePrivilegesById(userId);
		verify(userDao, never()).deleteUserById(userId);
	}

	@Test
	void testGetUserById() throws SQLException {
		Integer userId = 1;
		when(userDao.isIdFound(userId)).thenReturn(true);
		when(userDao.getUserById(userId)).thenReturn(returnUser());
		UserResponse response = userService.getUserById(userId);
		assertNotNull(response);
		assertEquals(StatusCode.SUCESSCODE.getCode(), response.getStatusCode());
		verify(userDao, times(1)).getUserById(userId);
	}

	@Test
	void testGetUserByIdNotFound() throws SQLException {
		Integer userId = 1;
		when(userDao.isIdFound(userId)).thenReturn(false);
		assertThrows(UserIdNotFoundException.class, () -> userService.getUserById(userId));
		verify(userDao, never()).getUserById(userId);
	}

	@Test
	void testGetAllUsers() throws SQLException {
		User user = returnUser();
		user.setUserId(1);
		User anotherUser = new User(2, "karthi", "karthikeyan", "karthi@gmail.com", "12345",
				Arrays.asList("mdu", "bng"), Arrays.asList("USER"), Arrays.asList("NO ACCESS"), true, true, true, true);
		when(userDao.getAllUsers()).thenReturn(Arrays.asList(user, anotherUser));
		assertEquals(2, userService.getAllUser().size());
		verify(userDao, times(1)).getAllUsers();
	}

	@Test
	void testGetUserByName() throws SQLException {
	when(userDao.getUserByUserName("kamal")).thenReturn(returnUser());
		User user=userService.getUserByUserName("kamal");
		assertNotNull(user);
		assertEquals("gfftff", user.getUserName());
		verify(userDao,times(1)).getUserByUserName("kamal");
	}

	@Test
	void testGetUserByNameNotFound() throws SQLException {
		when(userDao.getUserByUserName("velu")).thenReturn(null);
		assertThrows(UserIdNotFoundException.class,()-> userService.getUserByUserName("velu"));
	}

}
