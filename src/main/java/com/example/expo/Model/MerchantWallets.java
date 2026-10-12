package com.example.expo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class MerchantWallets {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @NotNull(message = "Balance is required")
    @PositiveOrZero(message = "Balance must be zero or greater")
    @Digits(integer = 10, fraction = 2)
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal balance ;//لازم نسند لها قيمة بالسيرفس

    @NotBlank(message = "Currency is required")
    @Size(max = 10, message = "Currency must not exceed 10 characters")
    @Column(nullable = false, length = 10)
    private String currency = "SAR";

    @NotBlank(message = "Status is required")
    @Pattern(
            regexp = "ACTIVE|SUSPENDED",
            message = "Status must be ACTIVE or SUSPENDED"
    )
    @Column(nullable = false, length = 9)
    private String status = "ACTIVE";

    @NotNull(message = "Created date is required")
    @PastOrPresent(message = "Created date cannot be in the future")
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    @NotNull(message = "Updated date is required")
    @PastOrPresent(message = "Updated date cannot be in the future")
    @Column(nullable = false)
    private LocalDateTime updated_at;





    /////////////////Relation

    // كل محفظة مرتبطة بتاجر واحد، وكل تاجر له محفظة واحدة كحد أقصى.
    @NotNull(message = "Merchant is required")
    @OneToOne(optional = false)
    @JoinColumn(name = "merchant_id", nullable = false, unique = true)
    @JsonIgnore
    private Merchants merchant;



}