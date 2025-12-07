package org.slavbx.productcatalog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.slavbx.productcatalog.dto.BrandDto;
import org.slavbx.productcatalog.model.Brand;

import java.util.List;

/**
 * Маппер для преобразования между сущностью Brand и DTO
 */
@Mapper(componentModel = "spring")
public interface BrandMapper {
    BrandMapper INSTANCE = Mappers.getMapper(BrandMapper.class);

    BrandDto brandToBrandDto(Brand brand);

    List<BrandDto> brandsToBrandDtos(List<Brand> brands);

    Brand brandDtoToBrand(BrandDto brandDTO);
}
