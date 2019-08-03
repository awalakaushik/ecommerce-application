package com.ecommerce.controllers;

import com.ecommerce.model.persistence.Cart;
import com.ecommerce.model.persistence.Item;
import com.ecommerce.model.persistence.User;
import com.ecommerce.model.persistence.repositories.CartRepository;
import com.ecommerce.model.persistence.repositories.ItemRepository;
import com.ecommerce.model.persistence.repositories.UserRepository;
import com.ecommerce.model.requests.ModifyCartRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ItemRepository itemRepository;

    private ResponseEntity<Cart> performCartOperation(String username, ModifyCartRequest body, OperationEnum operation) {
        User user = userRepository.findByUsername(username);
        if (null == user) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Optional<Item> item = itemRepository.findById(body.getItemId());
        if (!item.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Cart cart = user.getCart();
        switch (operation) {
            case ADD:
                IntStream.range(0, body.getQuantity()).forEach(i -> cart.addItem(item.get()));
                break;
            case REMOVE:
                IntStream.range(0, body.getQuantity()).forEach(i -> cart.removeItem(item.get()));
                break;
            default:
                break;
        }
        cartRepository.save(cart);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/addToCart")
    public ResponseEntity<Cart> addToCart(@RequestAttribute("username") String username, @RequestBody ModifyCartRequest body) {
        logger.info("Add item {}, quantity {} to cart for username {}", body.getItemId(), body.getQuantity(), username);
        return performCartOperation(username, body, OperationEnum.ADD);
    }

    @PostMapping("/removeFromCart")
    public ResponseEntity<Cart> removeFromCart(@RequestAttribute("username") String username, @RequestBody ModifyCartRequest body) {
        logger.info("Remove item {}, quantity {} from cart for username {}", body.getItemId(), body.getQuantity(), username);
        return performCartOperation(username, body, OperationEnum.REMOVE);
    }

    private enum OperationEnum {
        ADD, REMOVE
    }

}
