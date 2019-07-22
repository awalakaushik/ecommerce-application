package com.ecommerce.model.persistence.repositories;

import com.ecommerce.model.persistence.User;
import com.ecommerce.model.persistence.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<UserOrder, Long> {
    List<UserOrder> findByUser(User user);
}
