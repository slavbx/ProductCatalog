package org.slavbx.productcatalog.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.slavbx.productcatalog.dto.ProductDto;
import org.slavbx.productcatalog.model.Product;

import java.util.List;

/**
 * Маппер для преобразования между сущностью Product и DTO
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "sellerEmail", source = "seller.email")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "brandName", source = "brand.name")
    ProductDto productToProductDTO(Product product);

    List<ProductDto> productsToProductDTOs(List<Product> products);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "seller", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    Product productDTOToProduct(ProductDto productDTO);
}
