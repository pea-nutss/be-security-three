package com.aaronbujatin.securitythree.repository;

import com.aaronbujatin.securitythree.model.IUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<IUser, Long> {

    Optional<IUser> findByUsername(String username);

    Boolean existsByUsername(String username);
}
