package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.model.Message;

import com.openclassrooms.chatop.repository.MessageRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Data
@Service
@RequiredArgsConstructor
public class CustomMessageDetailsService {

    private final MessageRepository messageRepository;

    /**
     * Saves the provided message entity into the repository.
     *
     * @param message the message to be saved, containing user details, rental information,
     *                and the content of the message
     */
    public void saveMessage(Message message) {
        messageRepository.save(message);
    }
}
