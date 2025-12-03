package org.slavbx.productcatalog.controller;

import org.slavbx.productcatalog.annotation.Auditable;
import org.slavbx.productcatalog.dto.ProductDTO;
import org.slavbx.productcatalog.mapper.ProductMapper;
import org.slavbx.productcatalog.model.Product;
import org.slavbx.productcatalog.security.AuthenticationService;
import org.slavbx.productcatalog.service.BrandService;
import org.slavbx.productcatalog.service.CategoryService;
import org.slavbx.productcatalog.service.ProductService;
import org.slavbx.productcatalog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Контроллер для обработки HTTP-запросов к товарам.
 * Поддерживает получение, создание, обновление и удаление товаров.
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final AuthenticationService authService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final ProductMapper productMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public ProductController(ProductService productService,
                             AuthenticationService authService,
                             UserService userService,
                             CategoryService categoryService,
                             BrandService brandService,
                             ProductMapper productMapper,
                             ValidationUtil validationUtil) {
        this.productService = productService;
        this.authService = authService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.productMapper = productMapper;
        this.validationUtil = validationUtil;
    }

    @GetMapping
    @Auditable(action = "Получение всех товаров пользователя")
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productService.findAllProductsByUser(authService.getCurrentUser());
        return productMapper.productsToProductDTOs(products);
    }

    @GetMapping("/{id}")
    @Auditable(action = "Получение товара по id")
    public ProductDTO getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return productMapper.productToProductDTO(product);
    }

    @GetMapping("/name/{name}")
    @Auditable(action = "Получение товара по name")
    public ProductDTO getProductByName(@PathVariable String name) {
        Product product = productService.getProductByName(name);
        return productMapper.productToProductDTO(product);
    }

    @PostMapping
    @Auditable(action = "Создание товара")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        validationUtil.validate(productDTO);

        Product product = productMapper.productDTOToProduct(productDTO);
        product.setSeller(userService.getUserByEmail(productDTO.sellerEmail()));
        product.setCategory(categoryService.getCategoryByName(productDTO.categoryName()));
        product.setBrand(brandService.getBrandByName(productDTO.brandName()));
        product.setCreateDate(LocalDate.now());

        Product createdProduct = productService.create(product);
        ProductDTO createdProductDTO = productMapper.productToProductDTO(createdProduct);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdProductDTO);
    }

    @PutMapping
    @Auditable(action = "Сохранение товара")
    public ProductDTO updateProduct(@RequestBody ProductDTO productDTO) {
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
    @Auditable(action = "Удаление товара")
    public ResponseEntity<String> deleteProduct(@PathVariable String name) {
        productService.deleteByName(name);
        return ResponseEntity.ok("Successfully deleted");
    }
}
