package org.slavbx.productcatalog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slavbx.productcatalog.annotation.Auditable;
import org.slavbx.productcatalog.dto.BrandDto;
import org.slavbx.productcatalog.mapper.BrandMapper;
import org.slavbx.productcatalog.model.Brand;
import org.slavbx.productcatalog.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для обработки HTTP-запросов к брендам.
 * Поддерживает получение, создание, обновление и удаление брендов.
 */
@Tag(name = "BrandController", description = "API for working with brands")
@RestController
@RequestMapping("/brands")
public class BrandController {

    private final BrandService brandService;
    private final BrandMapper brandMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public BrandController(BrandService brandService,
                           BrandMapper brandMapper,
                           ValidationUtil validationUtil) {
        this.brandService = brandService;
        this.brandMapper = brandMapper;
        this.validationUtil = validationUtil;
    }

    @GetMapping
    @Operation(summary = "Get all brands")
    @Auditable(action = "Получение всех брендов")
    public List<BrandDto> getAllBrands() {
        List<Brand> brands = brandService.findAllBrands();
        return brandMapper.brandsToBrandDtos(brands);

    }

    @GetMapping("/{id}")
    @Operation(summary = "Get brand by id")
    @Auditable(action = "Получение бренда по id")
    public BrandDto getBrandById(@PathVariable Long id) {
        Brand brand = brandService.getBrandById(id);
        return brandMapper.brandToBrandDto(brand);
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get brand by name")
    @Auditable(action = "Получение бренда по name")
    public BrandDto getBrandByName(@PathVariable String name) {
        Brand brand = brandService.getBrandByName(name);
        return brandMapper.brandToBrandDto(brand);
    }

    @PostMapping
    @Operation(summary = "Create brand")
    @Auditable(action = "Создание бренда")
    public ResponseEntity<BrandDto> createBrand(@RequestBody BrandDto brandDTO) {
        validationUtil.validate(brandDTO);

        Brand brand = brandMapper.brandDtoToBrand(brandDTO);
        Brand createdBrand = brandService.create(brand);
        BrandDto createdBrandDto = brandMapper.brandToBrandDto(createdBrand);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBrandDto);
    }

    @PutMapping
    @Operation(summary = "Update brand")
    @Auditable(action = "Сохранение бренда")
    public BrandDto updateBrand(@RequestBody BrandDto brandDTO) {
        validationUtil.validate(brandDTO);
        Brand brand = brandMapper.brandDtoToBrand(brandDTO);
        Brand resultBrand = brandService.save(brand);
        return brandMapper.brandToBrandDto(resultBrand);
    }

    @DeleteMapping("/{name}")
    @Operation(summary = "Delete brand")
    @Auditable(action = "Удаление бренда")
    public ResponseEntity<String> deleteBrand(@PathVariable String name) {
        brandService.deleteByName(name);
        return ResponseEntity.ok("Successfully deleted");
    }
}
