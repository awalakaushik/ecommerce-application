package com.ecommerce.controllers;

import com.ecommerce.model.persistence.User;
import com.ecommerce.model.persistence.UserOrder;
import com.ecommerce.model.persistence.repositories.OrderRepository;
import com.ecommerce.model.persistence.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;


    @PostMapping("/submit")
    public ResponseEntity<UserOrder> submit(@RequestAttribute("username") String username) {
        User user = userRepository.findByUsername(username);
        if (null == user) {
            return ResponseEntity.notFound().build();
        }
        UserOrder order = UserOrder.createFromCart(user.getCart());
        orderRepository.save(order);
        logger.info("Submit order for username: {}", username);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/history")
    public ResponseEntity<List<UserOrder>> getOrdersForUser(@RequestAttribute("username") String username) {
        User user = userRepository.findByUsername(username);
        if (null == user) {
            return ResponseEntity.notFound().build();
        }
        logger.info("Return order history for username: {}", username);
        return ResponseEntity.ok(orderRepository.findByUser(user));
    }
}
