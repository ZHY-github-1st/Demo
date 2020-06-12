package com.example.user.repository;

import com.example.user.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Transactional
    long countById(Integer Id);

    @Override
    void delete(User user);

    @Override
    Optional<User> findById(Integer integer);
}
