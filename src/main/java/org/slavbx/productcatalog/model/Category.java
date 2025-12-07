package org.slavbx.productcatalog.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Класс, представляющий категорию товара.
 * Предоставляет информацию о названии, описании категории товара
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
//@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categories_seq")
    @SequenceGenerator(name = "categories_seq", sequenceName = "categories_id_seq", allocationSize = 1)
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "description")
    @EqualsAndHashCode.Exclude
    private String desc;

    @Override
    public String toString() {
        return String.format("| %-30s | %-60s |",
                "Категория: " + getName(),
                "Описание: " + getDesc());
    }
}