package org.slavbx.productcatalog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.slavbx.productcatalog.dto.BrandDTO;
import org.slavbx.productcatalog.model.Brand;

import java.util.List;

/**
 * Маппер для преобразования между сущностью Brand и DTO
 */
@Mapper(componentModel = "spring")
public interface BrandMapper {
    BrandMapper INSTANCE = Mappers.getMapper(BrandMapper.class);

    BrandDTO brandToBrandDTO(Brand brand);

    List<BrandDTO> brandsToBrandDTOs(List<Brand> brands);

    Brand brandDTOToBrand(BrandDTO brandDTO);
}
