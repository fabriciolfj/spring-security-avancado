package com.github.fabriciolfj.appexemplo1.repository;

import com.github.fabriciolfj.appexemplo1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByUsername(String u);
}
