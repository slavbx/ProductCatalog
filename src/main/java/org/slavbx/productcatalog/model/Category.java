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
public class Category {
    /**
     * Идентификатор
     */
    private Long id;
    /**
     * Название
     */
    @NonNull
    private String name;
    /**
     * Описание
     */
    private String desc;

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Category other = (Category) obj;
        return other.getName().equals(this.getName());
    }

    @Override
    public String toString() {
        return String.format("| %-30s | %-60s |",
                "Категория: " + getName(),
                "Описание: " + getDesc());
    }
}
