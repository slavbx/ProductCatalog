package org.slavbx.productcatalog.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slavbx.productcatalog.dto.CategoryDTO;
import org.slavbx.productcatalog.exception.NotFoundException;
import org.slavbx.productcatalog.mapper.CategoryMapper;
import org.slavbx.productcatalog.model.Category;
import org.slavbx.productcatalog.repository.RepositoryType;
import org.slavbx.productcatalog.service.CategoryService;
import org.slavbx.productcatalog.service.ServiceFactory;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/categories/*")
public class CategoryServlet extends BaseHttpServlet {
    private final CategoryService categoryService = ServiceFactory.getCategoryService(RepositoryType.JDBC);
    private final CategoryMapper categoryMapper = CategoryMapper.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        String pathInfo = httpReq.getRequestURI().substring(httpReq.getContextPath().length());

        if ("/categories".equals(pathInfo)) {
            doGetAllCategories(httpReq, httpResp);
        } else if (pathInfo.startsWith("/categories/")) {
            String endOfPath = pathInfo.substring("/categories/".length());
            if (endOfPath.matches("\\d+")) {
                doGetCategoryById(httpReq, httpResp, Long.parseLong(endOfPath));
            } else {
                doGetCategoryByName(httpReq, httpResp, URLDecoder.decode(endOfPath, StandardCharsets.UTF_8));
            }
        } else {
            throw new NotFoundException("Resource not found");
        }
    }

    protected void doGetCategoryById(HttpServletRequest httpReq, HttpServletResponse httpResp, Long id) throws IOException {
        Category category = categoryService.getCategoryById(id);
        CategoryDTO categoryDTO = categoryMapper.categoryToCategoryDTO(category);
        sendJsonResponse(httpResp, categoryDTO, HttpServletResponse.SC_OK);
    }

    protected void doGetCategoryByName(HttpServletRequest httpReq, HttpServletResponse httpResp, String name) throws IOException {
        Category category = categoryService.getCategoryByName(name);
        CategoryDTO categoryDTO = categoryMapper.categoryToCategoryDTO(category);
        sendJsonResponse(httpResp, categoryDTO, HttpServletResponse.SC_OK);
    }

    protected void doGetAllCategories(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        List<Category> categories = categoryService.findAllCategories();
        List<CategoryDTO> categoryDTOs = categoryMapper.categoriesToCategoryDTOs(categories);
        sendJsonResponse(httpResp, categoryDTOs, HttpServletResponse.SC_OK);
    }

    protected void doPost(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        String jsonReq = new String(httpReq.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        CategoryDTO categoryDTO = objectMapper.readValue(jsonReq, CategoryDTO.class);
        validate(categoryDTO);
        Category category = categoryMapper.categoryDTOToCategory(categoryDTO);

        Category createdCategory = categoryService.create(category);
        CategoryDTO createdCategoryDTO = categoryMapper.categoryToCategoryDTO(createdCategory);

        sendJsonResponse(httpResp, createdCategoryDTO, HttpServletResponse.SC_CREATED);
    }

    protected void doPut(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        String jsonReq = new String(httpReq.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        CategoryDTO categoryDTO = objectMapper.readValue(jsonReq, CategoryDTO.class);
        validate(categoryDTO);
        Category category = categoryMapper.categoryDTOToCategory(categoryDTO);

        Category resultCategory = categoryService.save(category);

        sendJsonResponse(httpResp, categoryMapper.categoryToCategoryDTO(resultCategory), HttpServletResponse.SC_OK);
    }

    @Override
    protected void doDelete(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        String pathInfo = httpReq.getRequestURI().substring(httpReq.getContextPath().length());
        if (pathInfo.startsWith("/categories/")) {
            String name = pathInfo.substring("/categories/".length());
            categoryService.deleteByName(name);
            sendSuccessResponse(httpResp, "Successfully deleted");
        } else {
            throw new NotFoundException("Resource not found");
        }
    }
}
