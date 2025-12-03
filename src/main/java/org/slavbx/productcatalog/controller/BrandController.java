package org.slavbx.productcatalog.controller;

import org.slavbx.productcatalog.annotation.Auditable;
import org.slavbx.productcatalog.dto.BrandDTO;
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
    @Auditable(action = "Получение всех брендов")
    public List<BrandDTO> getAllBrands() {
        List<Brand> brands = brandService.findAllBrands();
        return brandMapper.brandsToBrandDTOs(brands);
    }

    @GetMapping("/{id}")
    @Auditable(action = "Получение бренда по id")
    public BrandDTO getBrandById(@PathVariable Long id) {
        Brand brand = brandService.getBrandById(id);
        return brandMapper.brandToBrandDTO(brand);
    }

    @GetMapping("/name/{name}")
    @Auditable(action = "Получение бренда по name")
    public BrandDTO getBrandByName(@PathVariable String name) {
        Brand brand = brandService.getBrandByName(name);
        return brandMapper.brandToBrandDTO(brand);
    }

    @PostMapping
    @Auditable(action = "Создание бренда")
    public ResponseEntity<BrandDTO> createBrand(@RequestBody BrandDTO brandDTO) {
        validationUtil.validate(brandDTO);

        Brand brand = brandMapper.brandDTOToBrand(brandDTO);
        Brand createdBrand = brandService.create(brand);
        BrandDTO createdBrandDTO = brandMapper.brandToBrandDTO(createdBrand);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBrandDTO);
    }

    @PutMapping
    @Auditable(action = "Сохранение бренда")
    public BrandDTO updateBrand(@RequestBody BrandDTO brandDTO) {
        validationUtil.validate(brandDTO);
        Brand brand = brandMapper.brandDTOToBrand(brandDTO);
        Brand resultBrand = brandService.save(brand);
        return brandMapper.brandToBrandDTO(resultBrand);
    }

    @DeleteMapping("/{name}")
    @Auditable(action = "Удаление бренда")
    public ResponseEntity<String> deleteBrand(@PathVariable String name) {
        brandService.deleteByName(name);
        return ResponseEntity.ok("Successfully deleted");
    }
}
