package com.ecommerce.model.persistence.repositories;

import com.ecommerce.model.persistence.Cart;
import com.ecommerce.model.persistence.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
}
