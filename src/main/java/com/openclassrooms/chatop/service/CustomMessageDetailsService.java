package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.dto.MessageDTO;
import com.openclassrooms.chatop.model.Message;

import com.openclassrooms.chatop.model.Rental;
import com.openclassrooms.chatop.model.User;
import com.openclassrooms.chatop.repository.MessageRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Data
@Service
@RequiredArgsConstructor
public class CustomMessageDetailsService {

    private final MessageRepository messageRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomRentalDetailsService customRentalDetailsService;

    /**
     * Creates and saves a message entity based on the provided message details.
     *
     * @param messageDTO the data transfer object containing details about the message
     * @throws Exception if the user or rental specified in the messageDTO
     */
    public void createAndSaveMessage(MessageDTO messageDTO) throws Exception {
        User user = customUserDetailsService
                .getUser(Long.valueOf(messageDTO.getUser_id()))
                .orElseThrow(() -> new Exception("User not found !"));

        Rental rental = customRentalDetailsService.getRental(Long.valueOf(messageDTO.getRental_id()))
                .orElseThrow(() -> new Exception("Rental not found !"));

        if (messageDTO.getMessage() == null || messageDTO.getMessage().isEmpty()) {
            throw new Exception("Message content is empty !");
        }

        Message message = new Message();
        message.setUser(user);
        message.setRental(rental);
        message.setMessage(messageDTO.getMessage());

        messageRepository.save(message);
    }
}
