package org.slavbx.productcatalog.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Класс, представляющий бренд товара.
 * Предоставляет информацию о названии, описании бренда товара
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
//@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "brands")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "brands_seq")
    @SequenceGenerator(name = "brands_seq", sequenceName = "brands_id_seq", allocationSize = 1)
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(unique = true, nullable = false)
    @EqualsAndHashCode.Include
    private String name;

    @Column(name = "description")
    private String desc;

    @Override
    public String toString() {
        return String.format("| %-30s | %-20s |",
                "Бренд: " + getName(),
                "Описание: " + getDesc());
    }
}