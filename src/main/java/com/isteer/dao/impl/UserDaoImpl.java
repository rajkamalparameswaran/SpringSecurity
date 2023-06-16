package com.isteer.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.isteer.dao.layer.UserDao;
import com.isteer.module.User;
import com.isteer.spring.security.SpringSecurity;
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
		user.setUserPassword(new BCryptPasswordEncoder() .encode(user.getUserPassword()));

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
	public void updateUser(User user) {

		user.setUserPassword(new BCryptPasswordEncoder().encode(user.getUserPassword()));

		jdbcTemplate.update(SqlQueries.UPDATE_USER_BY_ADMIN, user.getUserName(), user.getUserFullName(), user.getUserEmail(),
				user.getUserPassword(),user.getIsAccountNonExpired(),user.getIsAccountNonLocked(),user.getIsCredentialsNonExpired(),user.getIsEnabled(), user.getUserId());

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
					user.setIsAccountNonExpired(rs.getString("isAccountNonExpired"));
					user.setIsAccountNonLocked(rs.getString("isAccountNonLocked"));
					user.setIsCredentialsNonExpired("isCredentialsNonExpired");
					user.setIsEnabled(rs.getString("isEnabled"));
					List<String> addresses = new ArrayList<String>();
					List<String> authorities = new ArrayList<>();
					addresses=jdbcTemplate.query(SqlQueries.GET_ADDRESS_BY_ID, new ResultSetExtractor<List<String>>() {

						@Override
						public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
							List<String> addresse = new ArrayList<String>();
							while (rs.next()) {
								addresse.add(rs.getString("address"));

							}

							return addresse;
						}

					}, userId);

					authorities=	jdbcTemplate.query(SqlQueries.GET_ROLES_BY_ID, new ResultSetExtractor<List<String>>() {

						@Override
						public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
							List<String> authoritie = new ArrayList<>();
							while (rs.next()) {
								authoritie.add(rs.getString("role"));

							}

							return authoritie;
						}

					}, userId);
					user.setUserAddresses(addresses);
					user.setUserRoles(authorities);

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
					user.setIsAccountNonExpired(rs.getString("isAccountNonExpired"));
					user.setIsAccountNonLocked(rs.getString("isAccountNonLocked"));
					user.setIsCredentialsNonExpired(rs.getString("isCredentialsNonExpired"));
					user.setIsEnabled(rs.getString("isEnabled"));
					List<String> addresses = new ArrayList<String>();
					List<String> authorities = new ArrayList<>();
					jdbcTemplate.query(SqlQueries.GET_ADDRESS_BY_ID, new ResultSetExtractor<>() {

						@Override
						public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
							while (rs.next()) {
								addresses.add(rs.getString("address"));

							}

							return null;
						}

					}, user.getUserId());

					jdbcTemplate.query(SqlQueries.GET_ROLES_BY_ID, new ResultSetExtractor<>() {

						@Override
						public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
							while (rs.next()) {
								authorities.add(rs.getString("role"));

							}

							return null;
						}

					}, user.getUserId());
					user.setUserAddresses(addresses);
					user.setUserRoles(authorities);

				}
				return user;
			}

		}, userName);

		return user;
	}

	
	@Override
	public Map<String,Boolean> duplicateEntry(User user) {
		
		return jdbcTemplate.query("SELECT USERNAME,USEREMAIL FROM USER WHERE USERID!=? ", new ResultSetExtractor<Map<String,Boolean>>(){

			@Override
			public Map<String, Boolean> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<String,Boolean> dublicates=new HashMap<>();
				boolean userNameFlag=false;
				boolean userEmailFlag=false;
				
				while(rs.next())
				{
					if(rs.getString("USERNAME").equalsIgnoreCase(user.getUserName()))
					{
						
						userNameFlag=true;
					}
					
					
					if(rs.getString("USEREMAIL").equalsIgnoreCase(user.getUserEmail()))
					{
						
						userEmailFlag=true;
					}
				
					
				}
			
				if(userNameFlag)
				{
					dublicates.put("userName", true);
				}
				else
				{
					dublicates.put("userName", false);
				}
				if(userEmailFlag)
				{
					dublicates.put("userEmail", true);
				}else
				{
					dublicates.put("userEmail", false);
				}
				
				return dublicates;
			}
			
		},user.getUserId());
		
	}

}
