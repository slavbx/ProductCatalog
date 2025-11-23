package org.slavbx.productcatalog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.slavbx.productcatalog.dto.CategoryDTO;
import org.slavbx.productcatalog.model.Category;

import java.util.List;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDTO categoryToCategoryDTO(Category category);

    List<CategoryDTO> categoriesToCategoryDTOs(List<Category> categories);

    Category categoryDTOToCategory(CategoryDTO categoryDTO);
}
