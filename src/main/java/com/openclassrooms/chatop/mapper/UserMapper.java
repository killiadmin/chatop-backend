package com.openclassrooms.chatop.mapper;

import com.openclassrooms.chatop.dto.UserDTO;
import com.openclassrooms.chatop.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole());
        userDTO.setCreated_at(user.getCreated_at());
        userDTO.setUpdated_at(user.getUpdated_at());
        return userDTO;
    }
}
