package com.uv.api_expedientes.Users;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    List<User> findByActivoTrue();

    Optional<User> findByUsername(String username);
}
