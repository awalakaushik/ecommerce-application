package com.ecommerce.model.persistence.repositories;

import java.util.List;

import com.ecommerce.model.persistence.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.model.persistence.User;

public interface OrderRepository extends JpaRepository<UserOrder, Long> {
	List<UserOrder> findByUser(User user);
}
