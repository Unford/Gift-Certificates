package com.epam.esm.core.repository.impl;

import com.epam.esm.core.repository.AbstractBaseRepository;
import com.epam.esm.core.model.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * It's a repository for GiftCertificate entities
 */
@Repository
public class OrderRepositoryImpl extends AbstractBaseRepository<Order> {
    public OrderRepositoryImpl() {
        super(Order.class);
    }

    @Override
    public Order update(Order entity) {
        throw new UnsupportedOperationException("Update command is forbidden for order repository");
    }

    @Override
    public Optional<Order> findByName(String name) {
        throw new UnsupportedOperationException("Find by name command is forbidden for order repository");
    }

    @Override
    public void deleteById(long id) {
        throw new UnsupportedOperationException("Delete command is forbidden for order repository");
    }
}
