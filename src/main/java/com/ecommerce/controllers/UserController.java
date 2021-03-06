package com.ecommerce.controllers;

import com.ecommerce.model.persistence.Cart;
import com.ecommerce.model.persistence.User;
import com.ecommerce.model.persistence.repositories.CartRepository;
import com.ecommerce.model.persistence.repositories.UserRepository;
import com.ecommerce.model.requests.CreateUserRequest;
import com.ecommerce.security.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    public UserController() {
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.of(userRepository.findById(id));
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        return null == user ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @GetMapping("/userInfo")
    public ResponseEntity<User> getUserInfo(@RequestAttribute("username") String username) {
        logger.info("Get information for username: {}", username);
        User user = userRepository.findByUsername(username);
        return null == user ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        Cart cart = new Cart();
        cartRepository.save(cart);
        user.setCart(cart);
        userRepository.save(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", tokenProvider.generateToken(user));
        logger.info("New user created with username: {}", user.getUsername());
        return new ResponseEntity<>(user, headers, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody CreateUserRequest createUserRequest) {
        User user = userRepository.findByUsername(createUserRequest.getUsername());
        if (Objects.isNull(user)) {
            return ResponseEntity.notFound().build();
        }
        if (!passwordEncoder.matches(createUserRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", tokenProvider.generateToken(user));
        logger.info("User logged in with username: {}", user.getUsername());
        return new ResponseEntity<>(user, headers, HttpStatus.OK);
    }
}
