package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.utils.GenerateCommon;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

public class OrderControllerTes {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void submitSuccess() {
        User user = GenerateCommon.createUser();
        Item item = GenerateCommon.createItem();

        Cart cart = new Cart();
        cart.setId(1L);
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        Mockito.when(userRepository.findByUsername("vops")).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit("vops");
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        UserOrder userOrder = response.getBody();
        Assert.assertNotNull(userOrder);
        Assert.assertNotNull(userOrder.getItems());
        Assert.assertNotNull(userOrder.getUser());
        Assert.assertNotNull(userOrder.getTotal());
    }

    @Test
    public void submitError() {
        User user = GenerateCommon.createUser();
        Item item = GenerateCommon.createItem();

        Cart cart = new Cart();
        cart.setId(1L);
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        ResponseEntity<UserOrder> response = orderController.submit("vops1");
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getOrdersForUserSuccess() {
        User user = GenerateCommon.createUser();
        Item item = GenerateCommon.createItem();

        Cart cart = new Cart();
        cart.setId(1L);
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        Mockito.when(userRepository.findByUsername("vops")).thenReturn(user);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(
                "vops");
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        List<UserOrder> list = response.getBody();
        Assert.assertNotNull(list);
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void getOrdersForUserError() {
        User user = GenerateCommon.createUser();
        Item item = GenerateCommon.createItem();

        Cart cart = new Cart();
        cart.setId(1L);
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(
                "vops1");
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
