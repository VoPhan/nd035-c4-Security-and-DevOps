package com.example.demo.controllers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

@RestController
@RequestMapping("/api/item")
public class ItemController {
	private static final Logger LOGGER = LogManager.getLogger(ItemController.class);

	@Autowired
	private ItemRepository itemRepository;
	
	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		LOGGER.info("Get all items");
		return ResponseEntity.ok(itemRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		LOGGER.info("Get item by id {}", id);
		Optional<Item> item = itemRepository.findById(id);
		if (item.isEmpty()) {
			LOGGER.error("Item not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		LOGGER.info("Get item by id {} success", id);
		return ResponseEntity.ok(item.get());
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		LOGGER.info("Get items by name {}", name);
		List<Item> items = itemRepository.findByName(name);
		if (Objects.isNull(items) || items.isEmpty()) {
			LOGGER.error("Items not found");
			return ResponseEntity.notFound().build();
		}
		LOGGER.info("Get items by name {} success", name);
		return ResponseEntity.ok(items);
	}
	
}
