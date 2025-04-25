package com.openclassrooms.chatop.repository;

import com.openclassrooms.chatop.model.Message;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends org.springframework.data.jpa.repository.JpaRepository<Message, Long> {
}
