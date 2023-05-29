package com.example.demo.controllers;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

@RestController
@RequestMapping("/api/order")
public class OrderController {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		LOGGER.debug("Submit order for user {}", username);
		User user = userRepository.findByUsername(username);
		if (Objects.isNull(user)) {
			LOGGER.error("User not found.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);
		LOGGER.debug("Submit order for user {} success", username);
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		LOGGER.debug("Get order for user {}", username);
		User user = userRepository.findByUsername(username);
		if (Objects.isNull(user)) {
			LOGGER.error("User not found.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		LOGGER.debug("Get order for user {} success", username);
		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}
