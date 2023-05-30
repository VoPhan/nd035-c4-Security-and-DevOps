package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
	private static final Logger LOGGER = LogManager.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		LOGGER.info("Find user by id {}", id);
		Optional<User> user = userRepository.findById(id);
		if (user.isEmpty()) {
			LOGGER.error("User not found.");
			return ResponseEntity.notFound().build();
		}
		LOGGER.info("Find user by id {} success", id);
		return ResponseEntity.ok(user.get());

	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		LOGGER.info("Find user by username {}", username);
		User user = userRepository.findByUsername(username);
		if (Objects.isNull(user)) {
			LOGGER.error("User not found.");
			return ResponseEntity.notFound().build();
		}
		LOGGER.info("Find user by username {} success", username);
		return ResponseEntity.ok(user);

	}

	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		LOGGER.info("Creating user {}", createUserRequest.getUsername());
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		if (createUserRequest.getPassword().length() < 7 || !createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
			LOGGER.error("Error with user password. Cannot create user {}", createUserRequest.getUsername());
			return ResponseEntity.badRequest().build();
		}
		Cart cart = new Cart();
		cart = cartRepository.save(cart);
		user.setCart(cart);
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
		user = userRepository.save(user);
		LOGGER.info("Creating user {} success", createUserRequest.getUsername());
		return ResponseEntity.ok(user);
	}
}
