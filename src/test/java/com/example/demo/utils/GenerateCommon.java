package com.example.demo.utils;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;

import java.math.BigDecimal;
import java.util.ArrayList;

public class GenerateCommon {

    public static User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("vops");
        user.setPassword("phanvo1999");
        return user;
    }

    public static Item createItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("New Item");
        item.setDescription("This is a new item");
        item.setPrice(new BigDecimal("9.99"));
        return item;
    }



}
