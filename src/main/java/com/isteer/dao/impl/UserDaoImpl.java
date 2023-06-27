package com.isteer.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.isteer.dao.layer.UserDao;
import com.isteer.module.EndPoint;
import com.isteer.module.User;
import com.isteer.sql.queries.SqlQueries;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public boolean isIdFound(Integer userId) {
		return jdbcTemplate.query(SqlQueries.GET_USER_BY_ID, new ResultSetExtractor<Boolean>() {
			@Override
			public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
				Integer foundedId = null;
				while (rs.next()) {
					foundedId = rs.getInt("userId");
				}
				return foundedId != null;
			}
		}, userId);
	}

	@Override
	public Integer addUser(User user) {
		KeyHolder holder = new GeneratedKeyHolder();
		user.setUserPassword(new BCryptPasswordEncoder().encode(user.getUserPassword()));
		jdbcTemplate.update(con -> {
			PreparedStatement ps = con.prepareStatement(SqlQueries.ADD_USER, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, user.getUserName());
			ps.setString(2, user.getUserFullName());
			ps.setString(3, user.getUserEmail());
			ps.setString(4, user.getUserPassword());
			return ps;
		}, holder);
		return holder.getKey().intValue();
	}

	@Override
	public void addAddresses(List<String> userAddresses, Integer userId) {
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
	}

	@Override
	public void addRoles(List<String> userRoles, Integer userId) {
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
	}

	@Override
	public void addPrivileges(List<String> userPrivileges, Integer userId) {
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
	}

	@Override
	public void updateUser(User user) {
		user.setUserPassword(new BCryptPasswordEncoder().encode(user.getUserPassword()));
		jdbcTemplate.update(SqlQueries.UPDATE_USER_BY_ADMIN, user.getUserName(), user.getUserFullName(),
				user.getUserEmail(), user.getUserPassword(), user.isAccountNonExpired(), user.isAccountNonLocked(),
				user.isCredentialsNonExpired(), user.isEnabled(), user.getUserId());
	}

	@Override
	public void deleteUserById(Integer userId) {
		jdbcTemplate.update(SqlQueries.DELETE_USER, userId);
	}

	@Override
	public void deleteAddressById(Integer userId) {
		jdbcTemplate.update(SqlQueries.DELETE_ADDRESS, userId);
	}

	@Override
	public void deleteRolesById(Integer userId) {
		jdbcTemplate.update(SqlQueries.DELETE_ROLES, userId);
	}

	@Override
	public void deletePrivilegesById(Integer userId) {
		jdbcTemplate.update(SqlQueries.DELETE_PRIVILEGES, userId);
	}

	@Override
	public User getUserById(Integer userId) {
		User user = jdbcTemplate.query(SqlQueries.GET_USER_BY_ID, new ResultSetExtractor<User>() {
			@Override
			public User extractData(ResultSet rs) throws SQLException, DataAccessException {
				User user = new User();
				while (rs.next()) {
					user.setUserId(rs.getInt("userId"));
					user.setUserName(rs.getString("userName"));
					user.setUserFullName(rs.getString("userFullName"));
					user.setUserEmail(rs.getString("userEmail"));
					user.setUserPassword(rs.getString("userPassword"));
					user.setAccountNonExpired(rs.getBoolean("isAccountNonExpired"));
					user.setAccountNonLocked(rs.getBoolean("isAccountNonLocked"));
					user.setCredentialsNonExpired(rs.getBoolean("isCredentialsNonExpired"));
					user.setEnabled(rs.getBoolean("isEnabled"));
					List<String> addresses = new ArrayList<String>();
					List<String> authorities = new ArrayList<>();
					List<String> privilege = new ArrayList<>();
					addresses = jdbcTemplate.query(SqlQueries.GET_ADDRESS_BY_ID,
							new ResultSetExtractor<List<String>>() {
								@Override
								public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
									List<String> addresse = new ArrayList<String>();
									while (rs.next()) {
										addresse.add(rs.getString("address"));
									}
									return addresse;
								}
							}, userId);
					authorities = jdbcTemplate.query(SqlQueries.GET_ROLES_BY_ID,
							new ResultSetExtractor<List<String>>() {
								@Override
								public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
									List<String> authoritie = new ArrayList<>();
									while (rs.next()) {
										authoritie.add(rs.getString("role"));
									}
									return authoritie;
								}
							}, userId);
					privilege = jdbcTemplate.query(SqlQueries.GET_PRIVILEGES, new ResultSetExtractor<List<String>>() {
						@Override
						public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
							List<String> authoritie = new ArrayList<>();
							while (rs.next()) {
								authoritie.add(rs.getString("privilege"));
							}
							return authoritie;
						}
					}, userId);
					user.setUserAddresses(addresses);
					user.setUserRoles(authorities);
					user.setPrivileges(privilege);
				}
				return user;
			}
		}, userId);
		return user;
	}

	@Override
	public List<Map<String, Object>> getAllUsers() {
		return jdbcTemplate.queryForList(SqlQueries.GET_ALL_USERS);
	}

	@Override
	public User getUserByUserName(String userName) {
		User user = jdbcTemplate.query(SqlQueries.GET_USER_BY_USERNAME, new ResultSetExtractor<User>() {
			@Override
			public User extractData(ResultSet rs) throws SQLException, DataAccessException {
				User user = new User();
				while (rs.next()) {
					user.setUserId(rs.getInt("userId"));
					user.setUserName(rs.getString("userName"));
					user.setUserFullName(rs.getString("userFullName"));
					user.setUserEmail(rs.getString("userEmail"));
					user.setUserPassword(rs.getString("userPassword"));
					user.setAccountNonExpired(rs.getBoolean("isAccountNonExpired"));
					user.setAccountNonLocked(rs.getBoolean("isAccountNonLocked"));
					user.setCredentialsNonExpired(rs.getBoolean("isCredentialsNonExpired"));
					user.setEnabled(rs.getBoolean("isEnabled"));
					List<String> addresses = new ArrayList<String>();
					List<String> authorities = new ArrayList<>();
					List<String> privilege = new ArrayList<>();
					addresses = jdbcTemplate.query(SqlQueries.GET_ADDRESS_BY_ID,
							new ResultSetExtractor<List<String>>() {
								@Override
								public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
									List<String> addresse = new ArrayList<String>();
									while (rs.next()) {
										addresse.add(rs.getString("address"));
									}
									return addresse;
								}
							}, user.getUserId());
					authorities = jdbcTemplate.query(SqlQueries.GET_ROLES_BY_ID,
							new ResultSetExtractor<List<String>>() {
								@Override
								public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
									List<String> authoritie = new ArrayList<>();
									while (rs.next()) {
										authoritie.add(rs.getString("role"));
									}
									return authoritie;
								}
							}, user.getUserId());
					privilege = jdbcTemplate.query(SqlQueries.GET_PRIVILEGES, new ResultSetExtractor<List<String>>() {
						@Override
						public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
							List<String> authoritie = new ArrayList<>();
							while (rs.next()) {
								authoritie.add(rs.getString("privilege"));
							}
							return authoritie;
						}
					}, user.getUserId());
					user.setUserAddresses(addresses);
					user.setUserRoles(authorities);
					user.setPrivileges(privilege);
					break;
				}
				return user;
			}
		}, userName);
		return user;
	}

	@Override
	public Map<String, Boolean> duplicateEntry(User user) {
		return jdbcTemplate.query("SELECT USERNAME,USEREMAIL FROM USER WHERE USERID!=? ",
				new ResultSetExtractor<Map<String, Boolean>>() {
					@Override
					public Map<String, Boolean> extractData(ResultSet rs) throws SQLException, DataAccessException {
						Map<String, Boolean> dublicates = new HashMap<>();
						boolean userNameFlag = false;
						boolean userEmailFlag = false;
						while (rs.next()) {
							if (rs.getString("USERNAME").equalsIgnoreCase(user.getUserName())) {
								userNameFlag = true;
							}
							if (rs.getString("USEREMAIL").equalsIgnoreCase(user.getUserEmail())) {
								userEmailFlag = true;
							}
						}
						if (userNameFlag) {
							dublicates.put("userName", true);
						} else {
							dublicates.put("userName", false);
						}
						if (userEmailFlag) {
							dublicates.put("userEmail", true);
						} else {
							dublicates.put("userEmail", false);
						}
						return dublicates;
					}
				}, user.getUserId());
	}

	@Override
	public void addPrivileges(Integer userId) {
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
	}

	@Override
	public List<String> getAddressByUserId(Integer userId) {
		return jdbcTemplate.query(SqlQueries.GET_ADDRESS_BY_USERID, new ResultSetExtractor<List<String>>() {
			@Override
			public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<String> addresses = new ArrayList<>();
				while (rs.next()) {
					addresses.add(rs.getString("address"));
				}
				return addresses;
			}
		}, userId);
	}

	@Override
	public String getAddressByUserIdAndAddressId(Integer userId, Integer addressId) {
		return jdbcTemplate.query(SqlQueries.GET_ADDRESS_BY_USERID_AND_ADDRESSID, new ResultSetExtractor<String>() {
			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				String address = null;
				while (rs.next()) {
					address = rs.getString("address");
				}
				return address;
			}
		}, userId, addressId);
	}

	@Override
	public String addressIdFounder(Integer addressId) {
		String address = null;
		address = jdbcTemplate.query(SqlQueries.ADDRESS_ID_FOUNDER, new ResultSetExtractor<String>() {
			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				String address = null;
				while (rs.next()) {
					address = rs.getString("address");
				}
				return address;
			}
		}, addressId);
		return address;
	}

	@Override
	public Integer addEndPoint(EndPoint endPoint) {
		KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(con -> {
			PreparedStatement ps = con.prepareStatement(SqlQueries.ADD_END_POINT,
					PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, endPoint.getEndPointName());
			return ps;
		}, holder);
		return holder.getKey().intValue();
	}

	@Override
	public void addAuthorization(List<String> authorities, Integer endPointId) {
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
	}

	@Override
	public void deleteAuthorization(Integer endPointId) {
		jdbcTemplate.update(SqlQueries.DELETE_AUTHORIZATION, endPointId);
	}

	@Override
	public String endPointIdFounder(Integer endPointId) {
		String endPointName = jdbcTemplate.query(SqlQueries.END_POINT_ID_FOUNDER, new ResultSetExtractor<String>() {
			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				String endPointName = null;
				while (rs.next()) {
					endPointName = rs.getString("endpointName");
				}
				return endPointName;
			}
		}, endPointId);
		return endPointName;
	}

	@Override
	public List<EndPoint> getAllEndPointDetails() {
		return jdbcTemplate.query(SqlQueries.GET_ALL_END_POINT, new ResultSetExtractor<List<EndPoint>>() {
			@Override
			public List<EndPoint> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<EndPoint> allEndPoints = new ArrayList<>();
				while (rs.next()) {
					EndPoint endPoint = new EndPoint();
					endPoint.setEndPointId(rs.getInt("endPointId"));
					endPoint.setEndPointName(rs.getString("endpointName"));
					List<String> privilege = new ArrayList<>();
					privilege = jdbcTemplate.query(SqlQueries.GET_ALL_AUTHORIZATION,
							new ResultSetExtractor<List<String>>() {

								@Override
								public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
									List<String> privilege = new ArrayList<>();
									while (rs.next()) {
										privilege.add(rs.getString("privilege"));
									}
									return privilege;
								}
							}, endPoint.getEndPointId());
					endPoint.setAuthorities(privilege);
					allEndPoints.add(endPoint);
				}
				return allEndPoints;
			}
		});
	}
}
