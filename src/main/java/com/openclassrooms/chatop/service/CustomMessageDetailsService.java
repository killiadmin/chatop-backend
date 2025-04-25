package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.model.Message;

import com.openclassrooms.chatop.repository.MessageRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Data
@Service
@RequiredArgsConstructor
public class CustomMessageDetailsService {

    private final MessageRepository messageRepository;

    public Iterable<Message> getMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessage(final Long id) {
        return messageRepository.findById(id);
    }

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public void deleteMessage(final Long id) {
        messageRepository.deleteById(id);
    }
}
