package org.slavbx.productcatalog.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slavbx.productcatalog.annotation.Auditable;
import org.slavbx.productcatalog.dto.ProductDTO;
import org.slavbx.productcatalog.exception.NotFoundException;
import org.slavbx.productcatalog.mapper.ProductMapper;
import org.slavbx.productcatalog.model.Product;
import org.slavbx.productcatalog.repository.RepositoryType;
import org.slavbx.productcatalog.security.AuthenticationService;
import org.slavbx.productcatalog.service.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

/**
 * Сервлет для обработки HTTP-запросов к товарам.
 * Поддерживает получение, создание, обновление и удаление товаров.
 * Доступные эндпоинты:
 * GET /products - получение всех товаров текущего пользователя
 * GET /products/{id} - получение товара по ID
 * GET /products/{name} - получение товара по имени
 * POST /products - создание нового товара
 * PUT /products - обновление существующего товара
 * DELETE /products/{name} - удаление товара по имени
 */
@WebServlet("/products/*")
public class ProductServlet extends BaseHttpServlet {
    RepositoryType repoType = RepositoryType.valueOf(System.getProperty("repository.type"));
    private final ProductService productService = ServiceFactory.getProductService(repoType);
    private final AuthenticationService authService = ServiceFactory.getAuthService(repoType);
    private final UserService userService = ServiceFactory.getUserService(repoType);
    private final CategoryService categoryService = ServiceFactory.getCategoryService(repoType);
    private final BrandService brandService = ServiceFactory.getBrandService(repoType);
    private final ProductMapper productMapper = ProductMapper.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        String pathInfo = httpReq.getRequestURI().substring(httpReq.getContextPath().length());

        if ("/products".equals(pathInfo)) {
            doGetAllProducts(httpReq, httpResp);
        } else if (pathInfo.startsWith("/products/")) {
            String endOfPath = pathInfo.substring("/products/".length());
            if (endOfPath.matches("\\d+")) {
                doGetProductById(httpReq, httpResp, Long.parseLong(endOfPath));
            } else {
                doGetProductByName(httpReq, httpResp, URLDecoder.decode(endOfPath, StandardCharsets.UTF_8));
            }
        } else {
            throw new NotFoundException("Resource not found");
        }
    }

    @Auditable(action = "Получение товара по id")
    protected void doGetProductById(HttpServletRequest httpReq, HttpServletResponse httpResp, Long id) throws IOException {
        Product product = productService.getProductById(id);
        ProductDTO productDTO = productMapper.productToProductDTO(product);
        sendJsonResponse(httpResp, productDTO, HttpServletResponse.SC_OK);
    }

    @Auditable(action = "Получение товара по name")
    protected void doGetProductByName(HttpServletRequest httpReq, HttpServletResponse httpResp, String name) throws IOException {
        Product product = productService.getProductByName(name);
        ProductDTO productDTO = productMapper.productToProductDTO(product);
        sendJsonResponse(httpResp, productDTO, HttpServletResponse.SC_OK);
    }

    @Auditable(action = "Получение всех товаров пользователя")
    protected void doGetAllProducts(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        List<Product> products = productService.findAllProductsByUser(authService.getCurrentUser());
        List<ProductDTO> productDTOs = productMapper.productsToProductDTOs(products);
        sendJsonResponse(httpResp, productDTOs, HttpServletResponse.SC_OK);
    }

    @Auditable(action = "Создание товара")
    protected void doPost(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        String jsonReq = new String(httpReq.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        ProductDTO productDTO = objectMapper.readValue(jsonReq, ProductDTO.class);
        validate(productDTO);

        Product product = productMapper.productDTOToProduct(productDTO);
        product.setSeller(userService.getUserByEmail(productDTO.sellerEmail()));
        product.setCategory(categoryService.getCategoryByName(productDTO.categoryName()));
        product.setBrand(brandService.getBrandByName(productDTO.brandName()));
        product.setCreateDate(LocalDate.now());

        Product createdProduct = productService.create(product);
        ProductDTO createdProductDTO = productMapper.productToProductDTO(createdProduct);

        sendJsonResponse(httpResp, createdProductDTO, HttpServletResponse.SC_CREATED);
    }

    @Auditable(action = "Сохранение товара")
    protected void doPut(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        String jsonReq = new String(httpReq.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        ProductDTO productDTO = objectMapper.readValue(jsonReq, ProductDTO.class);
        validate(productDTO);

        Product product = productMapper.productDTOToProduct(productDTO);
        product.setSeller(userService.getUserByEmail(productDTO.sellerEmail()));
        product.setCategory(categoryService.getCategoryByName(productDTO.categoryName()));
        product.setBrand(brandService.getBrandByName(productDTO.brandName()));
        if (product.getCreateDate() == null) {
            product.setCreateDate(LocalDate.now());
        }

        Product resultProduct = productService.save(product);

        sendJsonResponse(httpResp, productMapper.productToProductDTO(resultProduct), HttpServletResponse.SC_OK);
    }

    @Auditable(action = "Удаление товара")
    @Override
    protected void doDelete(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        String pathInfo = httpReq.getRequestURI().substring(httpReq.getContextPath().length());
        if (pathInfo.startsWith("/products/")) {
            String name = pathInfo.substring("/products/".length());
            productService.deleteByName(name);
            sendSuccessResponse(httpResp, "Successfully deleted");
        } else {
            throw new NotFoundException("Resource not found");
        }
    }
}
