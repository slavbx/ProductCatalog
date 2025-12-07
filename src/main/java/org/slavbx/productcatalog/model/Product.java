package org.slavbx.productcatalog.model;

import jakarta.persistence.*;
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
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_seq")
    @SequenceGenerator(name = "products_seq", sequenceName = "products_id_seq", allocationSize = 1)
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column
    private String name;

    @Column(name = "description")
    @EqualsAndHashCode.Exclude
    private String desc;

    @Column
    private BigDecimal price;

    @Column
    @EqualsAndHashCode.Exclude
    private Integer quantity;

    @Column(name = "create_date")
    @EqualsAndHashCode.Exclude
    private LocalDate createDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User seller;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
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

