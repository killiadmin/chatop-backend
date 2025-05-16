package com.openclassrooms.chatop.dto;

import com.openclassrooms.chatop.model.Rental;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RentalListDto {

    private List<Rental> rentals;

}
