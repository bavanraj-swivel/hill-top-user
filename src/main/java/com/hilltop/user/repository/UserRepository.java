package com.hilltop.user.repository;

import com.hilltop.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * User repository
 */
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByMobileNo(String mobileNo);
}
