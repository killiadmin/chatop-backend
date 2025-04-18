package com.openclassrooms.chatop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.openclassrooms.chatop.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
