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
@EqualsAndHashCode
public class Brand {
    @EqualsAndHashCode.Exclude
    private Long id;
    @NonNull
    @EqualsAndHashCode.Include
    private String name;
    private String desc;

    @Override
    public String toString() {
        return String.format("| %-30s | %-20s |",
                "Бренд: " + getName(),
                "Описание: " + getDesc());
    }
}