
package com.example.expo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Product name is required")
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String name;

    @Size(max = 1000)
    @Column(length = 1000)
    private String description;

    @NotNull(message = "Product price is required")
    @PositiveOrZero(message = "Price must be zero or greater")
    @Digits(integer = 10, fraction = 2)
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Size(max = 100)
    @Column(length = 100)
    private String category;

    // مسار أو رابط الصورة الأصلية، إن وجدت
    @URL
    @Size(max = 500)
    @Column(length = 500)
    private String imageUrl;

    @NotNull
    @Column(nullable = false)
    private Boolean available = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "merchant_id", nullable = false)
    @JsonIgnore
    private Merchants merchant;
}
