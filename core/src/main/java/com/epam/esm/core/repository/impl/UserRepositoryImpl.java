package com.epam.esm.core.repository.impl;

import com.epam.esm.core.repository.AbstractBaseRepository;
import com.epam.esm.core.model.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl extends AbstractBaseRepository<User> {

    public UserRepositoryImpl() {
        super(User.class);
    }

    @Override
    public User create(User entity) {
        throw new UnsupportedOperationException("Create command is forbidden for user repository");
    }

    @Override
    public Optional<User> update(User entity) {
        throw new UnsupportedOperationException("Update command is forbidden for user repository");
    }

    @Override
    public void deleteById(long id) {
        throw new UnsupportedOperationException("Delete command is forbidden for user repository");

    }
}
