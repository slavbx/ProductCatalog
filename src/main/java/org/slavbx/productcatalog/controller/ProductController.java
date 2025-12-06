package org.slavbx.productcatalog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slavbx.productcatalog.annotation.Auditable;
import org.slavbx.productcatalog.dto.ProductDto;
import org.slavbx.productcatalog.mapper.ProductMapper;
import org.slavbx.productcatalog.model.Product;
import org.slavbx.productcatalog.security.AuthenticationService;
import org.slavbx.productcatalog.service.BrandService;
import org.slavbx.productcatalog.service.CategoryService;
import org.slavbx.productcatalog.service.ProductService;
import org.slavbx.productcatalog.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Контроллер для обработки HTTP-запросов к товарам.
 * Поддерживает получение, создание, обновление и удаление товаров.
 */
@Tag(name = "ProductController", description = "API for working with products")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final AuthenticationService authService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final ProductMapper productMapper;
    private final ValidationUtil validationUtil;

    @GetMapping
    @Operation(summary = "Get all user products")
    @Auditable(action = "Получение всех товаров пользователя")
    public List<ProductDto> getAllProducts() {
        List<Product> products = productService.findAllProductsByUser(authService.getCurrentUser());
        return productMapper.productsToProductDTOs(products);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by id")
    @Auditable(action = "Получение товара по id")
    public ProductDto getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return productMapper.productToProductDTO(product);
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get product by name")
    @Auditable(action = "Получение товара по name")
    public ProductDto getProductByName(@PathVariable String name) {
        Product product = productService.getProductByName(name);
        return productMapper.productToProductDTO(product);
    }

    @PostMapping
    @Operation(summary = "Create product")
    @Auditable(action = "Создание товара")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDTO) {
        validationUtil.validate(productDTO);

        Product product = productMapper.productDTOToProduct(productDTO);
        product.setSeller(userService.getUserByEmail(productDTO.sellerEmail()));
        product.setCategory(categoryService.getCategoryByName(productDTO.categoryName()));
        product.setBrand(brandService.getBrandByName(productDTO.brandName()));
        product.setCreateDate(LocalDate.now());

        Product createdProduct = productService.create(product);
        ProductDto createdProductDto = productMapper.productToProductDTO(createdProduct);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdProductDto);
    }

    @PutMapping
    @Operation(summary = "Update product")
    @Auditable(action = "Сохранение товара")
    public ProductDto updateProduct(@RequestBody ProductDto productDTO) {
        validationUtil.validate(productDTO);

        Product product = productMapper.productDTOToProduct(productDTO);
        product.setSeller(userService.getUserByEmail(productDTO.sellerEmail()));
        product.setCategory(categoryService.getCategoryByName(productDTO.categoryName()));
        product.setBrand(brandService.getBrandByName(productDTO.brandName()));

        if (product.getCreateDate() == null) {
            product.setCreateDate(LocalDate.now());
        }

        Product resultProduct = productService.save(product);
        return productMapper.productToProductDTO(resultProduct);
    }

    @DeleteMapping("/{name}")
    @Operation(summary = "Delete product")
    @Auditable(action = "Удаление товара")
    public ResponseEntity<String> deleteProduct(@PathVariable String name) {
        productService.deleteByName(name);
        return ResponseEntity.ok("Successfully deleted");
    }
}
