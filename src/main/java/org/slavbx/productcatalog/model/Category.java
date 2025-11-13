package org.slavbx.productcatalog.model;

import lombok.*;

/**
 * Класс, представляющий категорию товара.
 * Предоставляет информацию о названии, описании категории товара
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class Category {
    @EqualsAndHashCode.Exclude
    private Long id;
    @NonNull
    private String name;
    @EqualsAndHashCode.Exclude
    private String desc;

    @Override
    public String toString() {
        return String.format("| %-30s | %-60s |",
                "Категория: " + getName(),
                "Описание: " + getDesc());
    }
}