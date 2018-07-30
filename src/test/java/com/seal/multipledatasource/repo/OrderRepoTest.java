package com.seal.multipledatasource.repo;


import com.seal.multipledatasource.config.OrderConfig;
import com.seal.multipledatasource.entity.order.Order;
import com.seal.multipledatasource.repo.order.OrderRepo;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = OrderConfig.class)
public class OrderRepoTest {

    @Autowired private OrderRepo orderRepo;

    @Test
    public void testOrderRepo() {
        Order order = orderRepo.save(new Order(null, "ACT LITE"));

        Assertions.assertThat(order).isNotNull();
        Assertions.assertThat(order.getId()).isNotZero();
    }

}