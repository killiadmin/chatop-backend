package com.openclassrooms.chatop.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "name", nullable = false, length = 255)
    @NotBlank(message = "Le nom est obligatoire")
    private String name;

    @Column(name = "surface")
    @Min(value = 1, message = "La surface doit être supérieure à 1 m²")
    private Integer surface;

    @Column(name = "price", precision = 10, scale = 2)
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être supérieur à 0")
    private BigDecimal price;

    @Lob
    @Column(name = "picture", columnDefinition = "LONGBLOB")
    private byte[] picture;

    @Column(name = "description", columnDefinition = "TEXT", length = 5000)
    private String description;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    @CreationTimestamp
    private LocalDateTime created_at;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updated_at;

    /**
     * Retrieves the ID of the owner associated with this rental.
     *
     * @return the ID of the owner, or null if no owner is set
     */
    public Long getOwnerId() {
        return this.owner != null ? this.owner.getId() : null;
    }

    public Object getCreatedAt() {
        return this.created_at;
    }

    public Object getUpdatedAt() {
        return this.updated_at;
    }
}
