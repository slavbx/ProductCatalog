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
public class Product {
    /**
     * Идентификатор
     */
    private Long id;
    /**
     * Название
     */
    private String name;
    /**
     * Описание
     */
    private String desc;
    /**
     * Стоимость
     */
    private BigDecimal price;
    /**
     * Дата создания
     */
    private LocalDate createDate;
    /**
     * Пользователь-продавец товара
     */
    private User seller;
    /**
     * Категория, в которую попадает товар
     */
    private Category category;
    /**
     * Бренд товара
     */
    private Brand brand;
    /**
     * Наличие на складе
     */
    private Integer quantity;

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Product other = (Product) obj;
        return other.getName().equals(this.getName());
    }

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

