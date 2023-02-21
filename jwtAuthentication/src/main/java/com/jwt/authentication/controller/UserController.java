package com.jwt.authentication.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.authentication.entity.User;
import com.jwt.authentication.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/registerNewUser")
	public User registerNewUser(@RequestBody User user) {
		return userService.registerNewUser(user);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/viewusers")
	public List<User> getAllUserDetails() {
		List<User> userlist = userService.getAllUserDetails();
		return userlist;
	}

	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@GetMapping("/user/{userId}")
	public User findUserDetailsById(@PathVariable("userId") int userId) {
		return userService.findUserDetailsById(userId);
	}

	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@PutMapping("/update/{userId}")
	public User updateUserDetailsById(@PathVariable(value = "userId") int userId, @RequestBody User user) {
		return userService.updateUserDetailsById(userId, user);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{userId}")
	public void deleteUserDetailsById(@PathVariable("userId") int userId) {
		userService.deletUserDetailsById(userId);
	}

}
