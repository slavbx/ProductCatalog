package org.slavbx.productcatalog.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Класс, представляющий товар.
 * Предоставляет информацию о названии, описании, стоимости,
 * дате создания, продавце, категории, бренде и наличии на складе
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Product {
    @EqualsAndHashCode.Exclude
    private Long id;
    private String name;

    @EqualsAndHashCode.Exclude
    private String desc;
    private BigDecimal price;

    @EqualsAndHashCode.Exclude
    private Integer quantity;

    @EqualsAndHashCode.Exclude
    private LocalDate createDate;
    private User seller;
    private Category category;
    private Brand brand;



    @Override
    public String toString() {
        return String.format("| %-30s | %-20s | %-20s | %-20s | %-25s |",
                "Товар: " + getName(),
                "Цена: " + getPrice(),
                "Количество: " + getQuantity(),
                "Категория: " + getCategory().getName(),
                "Бренд: " + getBrand().getName());
    }
}

