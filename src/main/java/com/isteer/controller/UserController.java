package com.isteer.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.isteer.exception.UserIdNotFoundException;
import com.isteer.jwt.token.JwtRequest;
import com.isteer.jwt.token.JwtRsponse;
import com.isteer.jwt.token.JwtUtil;
import com.isteer.message.properties.FailedMessage;
import com.isteer.module.EndPoint;
import com.isteer.module.User;
import com.isteer.service.impl.AddressResponse;
import com.isteer.service.impl.AddressesResponse;
import com.isteer.service.impl.AlternativeReturnUser;
import com.isteer.service.impl.EndPointResponse;
import com.isteer.service.impl.UserResponse;
import com.isteer.services.UserService;
import com.isteer.statuscode.StatusCode;

@RestController
public class UserController {
	
	@Autowired
	private FailedMessage property;
	
	@Autowired
	FailedMessage messageProperty;

	@Autowired
	UserService service;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtil util;

	@Autowired
	UserDetailsService userDetailsService;
	
	private static final Logger AUDITLOG=LogManager.getLogger("AuditLogs");

	@PostMapping("/addUser")
	public ResponseEntity<UserResponse> addUser(@RequestBody User user) {
		return new ResponseEntity<>(service.addUser(user), HttpStatus.CREATED);
	}

	@PutMapping("/updateUser")
	public ResponseEntity<AlternativeReturnUser> updateUser(@RequestBody User user) {

		return new ResponseEntity<>(service.updateUser(user), HttpStatus.ACCEPTED);
	}

	@PutMapping("/grandPermissions")
	public ResponseEntity<String> grantPermission(@RequestBody User user) {
		return new ResponseEntity<>(service.grantPermission(user), HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/deleteuserbyid/{userId}")
	public ResponseEntity<Map<String,Object>> updateUser(@PathVariable Integer userId) {
		return new ResponseEntity<>(service.deleteUserById(userId), HttpStatus.OK);
	}

	@GetMapping("/getuserbyid/{userId}")
	public ResponseEntity<UserResponse> getUserById(@PathVariable Integer userId) {
		return new ResponseEntity<>(service.getUserById(userId), HttpStatus.FOUND);
	}

	@GetMapping("/getalluser")
	public ResponseEntity<List<User>> getAllUser() {
		return new ResponseEntity<>(service.getAllUser(), HttpStatus.FOUND);
	}

	@GetMapping("/getuserbyname/{userName}")
	public ResponseEntity<User> getUserByName(@PathVariable String userName) {
		return new ResponseEntity<>(service.getUserByUserName(userName), HttpStatus.FOUND);
	}

	@PostMapping("/authenticate")
	public ResponseEntity<JwtRsponse> authenticat(@RequestBody JwtRequest request) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUserName(), request.getUserPassword()));
		} catch (Exception e) {
			List<String> exception = new ArrayList<>();
			exception.add(property.getWrongUserIdOrPassword());
			AUDITLOG.info(property.getWrongUserIdOrPassword());
			throw new UserIdNotFoundException(StatusCode.USERAUTHENTICATIONFAILED.getCode(),property.getLoginFailed(), exception);
		}
		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUserName());
		String jwt = util.generateToken(userDetails);
		JwtRsponse jwtRsponse = new JwtRsponse(jwt);
		return new ResponseEntity<>(jwtRsponse, HttpStatus.CREATED);
	}

	@GetMapping("/getaddressbyuserId/{userId}")
	public ResponseEntity<AddressesResponse> getGetAddressById(@PathVariable Integer userId) {
		return new ResponseEntity<>(service.getAddressByUserId(userId), HttpStatus.FOUND);

	}

	@GetMapping("/getaddressbyuserIdandaddressId/{userId}/{addressId}")
	public ResponseEntity<AddressResponse> getGetAddressById(@PathVariable Integer userId,
			@PathVariable Integer addressId) {
		return new ResponseEntity<>(service.getAddressByUserIdAndAddressId(userId, addressId),
				HttpStatus.FOUND);
	}

	@PostMapping("/addnewendpoint")
	public ResponseEntity<EndPointResponse> addNewEndPoint(@RequestBody EndPoint endPoint) {
		return new ResponseEntity<>(service.addNewEndPoint(endPoint), HttpStatus.CREATED);
	}

	@PutMapping("/updateendpointbyendpointid")
	public ResponseEntity<EndPointResponse> updateEndPointByEndPointId(@RequestBody EndPoint endPoint) {
		return new ResponseEntity<>(service.updateEndPointAccess(endPoint), HttpStatus.OK);
	}

	@GetMapping("/getAllEndPointDetails")
	public ResponseEntity<List<EndPoint>> getAllEndPoint() {
		return new ResponseEntity<>(service.getAllEndPointDetails(), HttpStatus.FOUND);
	}

}
