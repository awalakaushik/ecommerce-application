package com.ecommerce.controllers;

import com.ecommerce.model.persistence.Cart;
import com.ecommerce.model.persistence.Item;
import com.ecommerce.model.persistence.User;
import com.ecommerce.model.persistence.repositories.CartRepository;
import com.ecommerce.model.persistence.repositories.ItemRepository;
import com.ecommerce.model.persistence.repositories.UserRepository;
import com.ecommerce.model.requests.ModifyCartRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ItemRepository itemRepository;

    private ResponseEntity<Cart> performCartOperation(ModifyCartRequest request, OperationEnum operation) {
        User user = userRepository.findByUsername(request.getUsername());
        if (null == user) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Optional<Item> item = itemRepository.findById(request.getItemId());
        if (!item.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Cart cart = user.getCart();
        switch (operation) {
            case ADD:
                IntStream.range(0, request.getQuantity())
                        .forEach(i -> cart.addItem(item.get()));
            case REMOVE:
                IntStream.range(0, request.getQuantity())
                        .forEach(i -> cart.removeItem(item.get()));
            default:
                break;
        }
        cartRepository.save(cart);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/addToCart")
    public ResponseEntity<Cart> addToCart(@RequestBody ModifyCartRequest request) {
        return performCartOperation(request, OperationEnum.ADD);
    }

    @PostMapping("/removeFromCart")
    public ResponseEntity<Cart> removeFromCart(@RequestBody ModifyCartRequest request) {
        return performCartOperation(request, OperationEnum.REMOVE);
    }

    private enum OperationEnum {
        ADD, REMOVE
    }

}
