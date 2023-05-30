package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/order")
public class OrderController {
	private static final Logger LOGGER = LogManager.getLogger(OrderController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		LOGGER.info("Submit order for user {}", username);
		User user = userRepository.findByUsername(username);
		if (Objects.isNull(user)) {
			LOGGER.error("User not found.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);
		LOGGER.info("Submit order for user {} success", username);
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		LOGGER.info("Get order for user {}", username);
		User user = userRepository.findByUsername(username);
		if (Objects.isNull(user)) {
			LOGGER.error("User not found.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		LOGGER.info("Get order for user {} success", username);
		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}
