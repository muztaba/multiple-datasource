package com.seal.multipledatasource.repo.order;

import com.seal.multipledatasource.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long>{
}
