package org.slavbx.productcatalog.model;

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
@NoArgsConstructor
public class Brand {
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
        Brand other = (Brand) obj;
        return other.getName().equals(this.getName());
    }

    @Override
    public String toString() {
        return String.format("| %-30s | %-20s |",
                "Бренд: " + getName(),
                "Описание: " + getDesc());
    }
}
