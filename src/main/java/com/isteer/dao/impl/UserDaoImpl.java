package com.isteer.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isteer.dao.layer.UserDao;
import com.isteer.logs.Log4j2;
import com.isteer.message.properties.FailedMessage;
import com.isteer.module.EndPoint;
import com.isteer.module.User;
import com.isteer.sql.queries.SqlQueries;
import com.isteer.table.details.UserTableDetails;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	FailedMessage failedMsg;

	@Override
	public boolean isIdFound(Integer userId) throws SQLException {

		try {
			String userData = jdbcTemplate.queryForObject(SqlQueries.GET_USER_BY_ID, String.class, userId);
			return userData != null;
		} catch (Exception e) {
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SQLException(e.getLocalizedMessage());
		}

	}

	@Override
	public Integer addUser(User user) throws SQLException {

		try {
			KeyHolder holder = new GeneratedKeyHolder();
			user.setUserPassword(new BCryptPasswordEncoder().encode(user.getUserPassword()));
			jdbcTemplate.update(con -> {
				PreparedStatement ps = con.prepareStatement(SqlQueries.ADD_USER, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, user.getUserName());
				ps.setString(2, user.getUserFullName());
				ps.setString(3, user.getUserEmail());
				ps.setString(4, user.getUserPassword());
				return ps;
			}, holder);
			return holder.getKey().intValue();
		} catch (Exception e) {
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SQLException(e.getLocalizedMessage());
		}
	}

	@Override
	public void addAddresses(List<String> userAddresses, Integer userId) throws SQLException {
		try {
			jdbcTemplate.batchUpdate(SqlQueries.ADD_ADDRESS, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setInt(1, userId);
					ps.setString(2, userAddresses.get(i));
				}

				@Override
				public int getBatchSize() {
					return userAddresses.size();
				}
			});
		} catch (Exception e) {
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SQLException(e.getLocalizedMessage());
		}
	}

	@Override
	public void addRoles(List<String> userRoles, Integer userId) throws SQLException {
		try {
			jdbcTemplate.batchUpdate(SqlQueries.ADD_ROLES, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setInt(1, userId);
					ps.setString(2, userRoles.get(i));
				}

				@Override
				public int getBatchSize() {
					return userRoles.size();
				}
			});
		} catch (Exception e) {
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SQLException(e.getLocalizedMessage());
		}
	}

	@Override
	public void addPrivileges(List<String> userPrivileges, Integer userId) throws SQLException {
		try {
			jdbcTemplate.batchUpdate(SqlQueries.INSERT_PRIVILEGES, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setInt(1, userId);
					ps.setString(2, userPrivileges.get(i));
				}

				@Override
				public int getBatchSize() {
					return userPrivileges.size();
				}
			});
		} catch (Exception e) {
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SQLException(e.getLocalizedMessage());
		}
	}

	@Override
	public void updateUser(User user) throws SQLException {
		try {
			user.setUserPassword(new BCryptPasswordEncoder().encode(user.getUserPassword()));
			jdbcTemplate.update(SqlQueries.UPDATE_USER_BY_ADMIN, user.getUserName(), user.getUserFullName(),
					user.getUserEmail(), user.getUserPassword(), user.isAccountNonExpired(), user.isAccountNonLocked(),
					user.isCredentialsNonExpired(), user.isEnabled(), user.getUserId());
		} catch (Exception e) {
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SQLException(e.getLocalizedMessage());
		}
	}

	@Override
	public void deleteUserById(Integer userId) throws SQLException {
		try {
			jdbcTemplate.update(SqlQueries.DELETE_USER, userId);
		} catch (Exception e) {
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SQLException(e.getLocalizedMessage());
		}
	}

	@Override
	public void deleteAddressById(Integer userId) throws SQLException {
		try {
			jdbcTemplate.update(SqlQueries.DELETE_ADDRESS, userId);
		} catch (Exception e) {
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SQLException(e.getLocalizedMessage());
		}
	}

	@Override
	public void deleteRolesById(Integer userId) throws SQLException {
		try {
			jdbcTemplate.update(SqlQueries.DELETE_ROLES, userId);
		} catch (Exception e) {
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SQLException(e.getLocalizedMessage());
		}
	}

	@Override
	public void deletePrivilegesById(Integer userId) throws SQLException {
		try {
			jdbcTemplate.update(SqlQueries.DELETE_PRIVILEGES, userId);
		} catch (Exception e) {
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SQLException(e.getLocalizedMessage());
		}
	}

	@Override
	public User getUserById(Integer userId) throws SQLException {
		try {
			String userData = jdbcTemplate.queryForObject(SqlQueries.GET_USER_BY_ID, String.class, userId);
			ObjectMapper mapper = new ObjectMapper();
			User user = null;
			user = mapper.readValue(userData, User.class);
			return user;
		} catch (Exception e) {
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SQLException(e.getLocalizedMessage());
		}
	}

	@Override
	public List<User> getAllUsers() throws SQLException {
		try {
			String users = jdbcTemplate.queryForObject(SqlQueries.GET_ALL_USERS, String.class);
			List<User> user = new ArrayList<>();
			if (users != null) {
				ObjectMapper mapper = new ObjectMapper();
				user = mapper.readValue(users, new TypeReference<List<User>>() {
				});
				return user;
			}
			return user;
		} catch (Exception e) {
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SQLException(e.getLocalizedMessage());
		}
	}

	@Override
	public User getUserByUserName(String userName) throws SQLException {
		try {
			String userData = jdbcTemplate.queryForObject(SqlQueries.GET_USER_BY_USERNAME, String.class, userName);
			if(userData!=null)
			{
				ObjectMapper mapper = new ObjectMapper();
				User user = null;
				user = mapper.readValue(userData, User.class);
				return user;
			}
			return null;
		} catch (Exception e) {
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SQLException(e.getLocalizedMessage());
		}
	}

	@Override
	public void addPrivileges(Integer userId) throws SQLException {
		try {
			jdbcTemplate.batchUpdate(SqlQueries.INSERT_USER_PRIVILEGES, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setInt(1, userId);
				}

				@Override
				public int getBatchSize() {
					return 1;
				}
			});
		} catch (Exception e) {
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SQLException(e.getLocalizedMessage());
		}
	}

	@Override
	public List<String> getAddressByUserId(Integer userId) throws SQLException {
		try {
			return jdbcTemplate.query(SqlQueries.GET_ADDRESS_BY_USERID, new ResultSetExtractor<List<String>>() {
				@Override
				public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<String> addresses = new ArrayList<>();
					while (rs.next()) {
						addresses.add(rs.getString(UserTableDetails.ADDRESS_COLUMN_NAME));
					}
					return addresses;
				}
			}, userId);
		} catch (Exception e) {
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SQLException(e.getLocalizedMessage());
		}
	}

	@Override
	public String getAddressByUserIdAndAddressId(Integer userId, Integer addressId) throws SQLException {
		try {
			return jdbcTemplate.query(SqlQueries.GET_ADDRESS_BY_USERID_AND_ADDRESSID, new ResultSetExtractor<String>() {
				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					String address = null;
					while (rs.next()) {
						address = rs.getString(UserTableDetails.ADDRESS_COLUMN_NAME);
					}
					return address;
				}
			}, userId, addressId);
		} catch (Exception e) {
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SQLException(e.getLocalizedMessage());
		}
	}

	@Override
	public String addressIdFounder(Integer addressId) throws SQLException {
		try {
			String address = null;
			address = jdbcTemplate.query(SqlQueries.ADDRESS_ID_FOUNDER, new ResultSetExtractor<String>() {
				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					String address = null;
					while (rs.next()) {
						address = rs.getString(UserTableDetails.ADDRESS_COLUMN_NAME);
					}
					return address;
				}
			}, addressId);
			return address;
		} catch (Exception e) {
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SQLException(e.getLocalizedMessage());
		}
	}

	@Override
	public Integer addEndPoint(EndPoint endPoint) throws SQLException {
		try {
			KeyHolder holder = new GeneratedKeyHolder();
			jdbcTemplate.update(con -> {
				PreparedStatement ps = con.prepareStatement(SqlQueries.ADD_END_POINT, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, endPoint.getEndPointName());
				return ps;
			}, holder);
			return holder.getKey().intValue();
		} catch (Exception e) {
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SQLException(e.getLocalizedMessage());
		}
	}

	@Override
	public void addAuthorization(List<String> authorities, Integer endPointId) throws SQLException {
		try {
			jdbcTemplate.batchUpdate(SqlQueries.ADD_AUTHORIZATION, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setInt(1, endPointId);
					ps.setString(2, authorities.get(i));
				}

				@Override
				public int getBatchSize() {
					return authorities.size();
				}
			});
		} catch (Exception e) {
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SQLException(e.getLocalizedMessage());
		}
	}

	@Override
	public void deleteAuthorization(Integer endPointId) throws SQLException {
		try {
			jdbcTemplate.update(SqlQueries.DELETE_AUTHORIZATION, endPointId);
		} catch (Exception e) {
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SQLException(e.getLocalizedMessage());
		}
	}

	@Override
	public String endPointIdFounder(Integer endPointId) throws SQLException {
		try {
			return jdbcTemplate.query(SqlQueries.END_POINT_ID_FOUNDER, new ResultSetExtractor<String>() {
				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					String endPointName = null;
					while (rs.next()) {
						endPointName = rs.getString(UserTableDetails.ENDPOINTNAME_COLUMN_NAME);
					}
					return endPointName;
				}
			}, endPointId);
		} catch (Exception e) {
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SQLException(e.getLocalizedMessage());
		}

	}

	@Override
	public List<EndPoint> getAllEndPointDetails() throws SQLException {
		try {
			String endPoints = jdbcTemplate.queryForObject(SqlQueries.GET_ALL_END_POINT, String.class);
			List<EndPoint> listOfEndPoints = new ArrayList<>();
			if (endPoints != null) {
				ObjectMapper mapper = new ObjectMapper();
				listOfEndPoints = mapper.readValue(endPoints, new TypeReference<List<EndPoint>>() {
				});
				return listOfEndPoints;
			}
			return listOfEndPoints;
		} catch (Exception e) {
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SQLException(e.getLocalizedMessage());
		}
	}

	@Override
	public int toCheckDuplicateUserName(String userName, int userId) throws SQLException {
		try {
			return jdbcTemplate.queryForObject(SqlQueries.GET_DUBLICATE_USERNAME, Integer.class, userName, userId);
		} catch (Exception e) {
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SQLException(e.getLocalizedMessage());
		}
	}

	@Override
	public int toCheckDuplicateUserEmail(String userEmail, int userId) throws SQLException {
		try {
			return jdbcTemplate.queryForObject(SqlQueries.GET_DUBLICATE_EMAIL, Integer.class, userEmail, userId);
		} catch (Exception e) {
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SQLException(e.getLocalizedMessage());
		}
	}

	@Override
	public List<String> getAuthoriesByUserId(Integer userId) throws SQLException {
		try {
			return jdbcTemplate.query(SqlQueries.GET_ROLES_BY_ID, new ResultSetExtractor<List<String>>() {
				@Override
				public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<String> authoritie = new ArrayList<>();
					while (rs.next()) {
						authoritie.add(rs.getString(UserTableDetails.ROLE_COLUMN_NAME));
					}
					return authoritie;
				}
			}, userId);
		} catch (Exception e) {
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SQLException(e.getLocalizedMessage());
		}
	}

	@Override
	public List<String> getPrivilegesByUserId(Integer userId) throws SQLException {
		try {
			return jdbcTemplate.query(SqlQueries.GET_PRIVILEGES, new ResultSetExtractor<List<String>>() {
				@Override
				public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<String> authoritie = new ArrayList<>();
					while (rs.next()) {
						authoritie.add(rs.getString(UserTableDetails.PRIVILEGE_COLUMN_NAME));
					}
					return authoritie;
				}
			}, userId);
		} catch (Exception e) {
			Log4j2.getAuditlog().error(e.getLocalizedMessage());
			throw new SQLException(e.getLocalizedMessage());
		}
	}
}
