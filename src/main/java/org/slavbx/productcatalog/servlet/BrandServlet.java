package org.slavbx.productcatalog.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slavbx.productcatalog.dto.BrandDTO;
import org.slavbx.productcatalog.exception.NotFoundException;
import org.slavbx.productcatalog.mapper.BrandMapper;
import org.slavbx.productcatalog.model.Brand;
import org.slavbx.productcatalog.repository.RepositoryType;
import org.slavbx.productcatalog.service.BrandService;
import org.slavbx.productcatalog.service.ServiceFactory;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;


@WebServlet("/brands/*")
public class BrandServlet extends BaseHttpServlet {
    private final BrandService brandService = ServiceFactory.getBrandService(RepositoryType.JDBC);
    private final BrandMapper brandMapper = BrandMapper.INSTANCE;


    @Override
    protected void doGet(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        String pathInfo = httpReq.getRequestURI().substring(httpReq.getContextPath().length());

        if ("/brands".equals(pathInfo)) {
            doGetAllBrands(httpReq, httpResp);
        } else if (pathInfo.startsWith("/brands/")) {
            String endOfPath = pathInfo.substring("/brands/".length());
            if (endOfPath.matches("\\d+")) {
                doGetBrandById(httpReq, httpResp, Long.parseLong(endOfPath));
            } else {
                doGetBrandByName(httpReq, httpResp, URLDecoder.decode(endOfPath, StandardCharsets.UTF_8));
            }
        } else {
            throw new NotFoundException("Resource not found");
        }
    }

    protected void doGetBrandById(HttpServletRequest httpReq, HttpServletResponse httpResp, Long id) throws IOException {
        Brand brand = brandService.getBrandById(id);
        BrandDTO brandDTO = brandMapper.brandToBrandDTO(brand);
        sendJsonResponse(httpResp, brandDTO, HttpServletResponse.SC_OK);
    }

    protected void doGetBrandByName(HttpServletRequest httpReq, HttpServletResponse httpResp, String name) throws IOException {
        Brand brand = brandService.getBrandByName(name);
        BrandDTO brandDTO = brandMapper.brandToBrandDTO(brand);
        sendJsonResponse(httpResp, brandDTO, HttpServletResponse.SC_OK);
    }

    protected void doGetAllBrands(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        List<Brand> brands = brandService.findAllBrands();
        List<BrandDTO> brandDTOs = brandMapper.brandsToBrandDTOs(brands);
        sendJsonResponse(httpResp, brandDTOs, HttpServletResponse.SC_OK);
    }

    protected void doPost(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        String jsonReq = new String(httpReq.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        BrandDTO brandDTO = objectMapper.readValue(jsonReq, BrandDTO.class);
        validate(brandDTO);
        Brand brand = brandMapper.brandDTOToBrand(brandDTO);

        Brand createdBrand = brandService.create(brand);
        BrandDTO createdBrandDTO = brandMapper.brandToBrandDTO(createdBrand);

        sendJsonResponse(httpResp, createdBrandDTO, HttpServletResponse.SC_CREATED);
    }

    protected void doPut(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        String jsonReq = new String(httpReq.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        BrandDTO brandDTO = objectMapper.readValue(jsonReq, BrandDTO.class);
        Brand brand = brandMapper.brandDTOToBrand(brandDTO);

        Brand resultBrand = brandService.save(brand);

        sendJsonResponse(httpResp, brandMapper.brandToBrandDTO(resultBrand), HttpServletResponse.SC_OK);
    }

    @Override
    protected void doDelete(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        String pathInfo = httpReq.getRequestURI().substring(httpReq.getContextPath().length());
        if (pathInfo.startsWith("/brands/")) {
            String name = pathInfo.substring("/brands/".length());
            brandService.deleteByName(name);
            sendSuccessResponse(httpResp, "Successfully deleted");
        } else {
            throw new NotFoundException("Resource not found");
        }
    }
}
