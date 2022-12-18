package com.backend.test.controller;

import com.backend.test.model.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.backend.test.model.User;
import com.backend.test.service.UserService;
import com.backend.test.util.CustomErrorType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class UserController {
	public static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService userService; 
	
	
	@RequestMapping(value = "/get-user/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getUserById(@PathVariable("id") long id) {
		logger.info("Fetching User with id {}", id);
		User user = userService.findById(id);
		if (user == null) {
			logger.error("User with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("User with id " + id 
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	
	@RequestMapping(value = "/create-user/", method = RequestMethod.POST)
	public ResponseEntity<?> createUser(@RequestBody User user) {
		logger.info("Creating User : {}", user);

		if (userService.isUserExist(user)) {
			logger.error("Unable to create. A User with firstname {} already exist", user.getFirstname());
			return new ResponseEntity(new CustomErrorType("Unable to create. A User with firstname " + 
			user.getFirstname() + " already exist."),HttpStatus.CONFLICT);
		}

		userService.saveUser(user);
		Response response=new Response(true,"User created!");
		return new ResponseEntity(response, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/update-user/", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUser(@RequestBody User user) {
		logger.info("Updating User with id {}", user);

		User currentUser = userService.findById(user.getId());

		if (currentUser == null) {
			logger.error("Unable to update. User with id {} not found.", user.getId());
			return new ResponseEntity(new CustomErrorType("Unable to upate. User with id " + user.getId() + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentUser.setFirstname(user.getFirstname());
		currentUser.setLastname(user.getLastname());
		currentUser.setContactNum(user.getContactNum());

		userService.updateUser(currentUser);
		return new ResponseEntity<User>(currentUser, HttpStatus.OK);
	}
	

	@RequestMapping(value = "/delete-user/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
		logger.info("Fetching & Deleting User with id {}", id);

		User user = userService.findById(id);
		if (user == null) {
			logger.error("Unable to delete. User with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. User with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		userService.deleteUserById(id);
		Response response=new Response(true,"User deleted!");
		return new ResponseEntity(response, HttpStatus.OK);
	}

}
