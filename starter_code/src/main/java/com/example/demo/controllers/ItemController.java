package com.example.demo.controllers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);

	@Autowired
	private ItemRepository itemRepository;
	
	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		return ResponseEntity.ok(itemRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		LOGGER.debug("Get item by id {}", id);
		Optional<Item> item = itemRepository.findById(id);
		if (item.isEmpty()) {
			LOGGER.error("Item not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		LOGGER.debug("Get item by id {} success", id);
		return ResponseEntity.ok(item.get());
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		LOGGER.debug("Get items by name {}", name);
		List<Item> items = itemRepository.findByName(name);
		if (Objects.isNull(items) || items.isEmpty()) {
			LOGGER.error("Items not found");
			return ResponseEntity.notFound().build();
		}
		LOGGER.debug("Get items by name {} success", name);
		return ResponseEntity.ok(items);
	}
	
}
